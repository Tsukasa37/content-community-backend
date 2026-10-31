package com.community.service;

import com.community.config.SecurityUtils;
import com.community.dto.ArticleRequest;
import com.community.dto.ArticleResponse;
import com.community.entity.Article;
import com.community.entity.User;
import com.community.exception.BusinessException;
import com.community.repository.ArticleRepository;
import com.community.repository.CommentRepository;
import com.community.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;

    public ArticleService(ArticleRepository articleRepository,
                          CommentRepository commentRepository,
                          UserRepository userRepository,
                          SecurityUtils securityUtils) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.securityUtils = securityUtils;
    }

    @Transactional
    public ArticleResponse create(ArticleRequest request) {
        User current = securityUtils.currentUser();
        Article article = new Article();
        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setUser(current);
        articleRepository.save(article);
        return toResponse(article);
    }

    @Transactional(readOnly = true)
    public Page<ArticleResponse> list(int page, int size) {
        Pageable pageable = PageRequest.of(
                Math.max(page - 1, 0), size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return articleRepository.findAllByOrderByCreatedAtDesc(pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public ArticleResponse getById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "文章不存在"));
        return toResponse(article);
    }

    @Transactional
    public ArticleResponse update(Long id, ArticleRequest request) {
        User current = securityUtils.currentUser();
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "文章不存在"));
        if (!article.getUser().getId().equals(current.getId())) {
            throw new BusinessException(403, "只有作者本人可以修改");
        }
        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        return toResponse(article);
    }

    @Transactional
    public void delete(Long id) {
        User current = securityUtils.currentUser();
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "文章不存在"));
        if (!article.getUser().getId().equals(current.getId())) {
            throw new BusinessException(403, "只有作者本人可以删除");
        }
        articleRepository.delete(article);
    }

    private ArticleResponse toResponse(Article article) {
        long commentCount = commentRepository.countByArticleId(article.getId());
        return new ArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getUser().getId(),
                article.getUser().getNickname(),
                commentCount,
                article.getCreatedAt());
    }
}
