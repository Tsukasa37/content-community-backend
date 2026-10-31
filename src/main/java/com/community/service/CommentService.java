package com.community.service;

import com.community.config.SecurityUtils;
import com.community.dto.CommentRequest;
import com.community.dto.CommentResponse;
import com.community.entity.Article;
import com.community.entity.Comment;
import com.community.entity.User;
import com.community.exception.BusinessException;
import com.community.repository.ArticleRepository;
import com.community.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final SecurityUtils securityUtils;

    public CommentService(CommentRepository commentRepository,
                          ArticleRepository articleRepository,
                          SecurityUtils securityUtils) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
        this.securityUtils = securityUtils;
    }

    @Transactional
    public CommentResponse create(Long articleId, CommentRequest request) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessException(404, "文章不存在"));
        User current = securityUtils.currentUser();

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setArticle(article);
        comment.setUser(current);
        commentRepository.save(comment);
        return toResponse(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> listByArticle(Long articleId) {
        return commentRepository.findByArticleIdOrderByCreatedAtAsc(articleId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        User current = securityUtils.currentUser();
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "评论不存在"));
        if (!comment.getUser().getId().equals(current.getId())) {
            throw new BusinessException(403, "只有评论作者本人可以删除");
        }
        commentRepository.delete(comment);
    }

    private CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getArticle().getId(),
                comment.getUser().getId(),
                comment.getUser().getNickname(),
                comment.getCreatedAt());
    }
}
