package com.community.service;

import com.community.dto.ArticleRequest;
import com.community.dto.ArticleResponse;
import com.community.entity.User;
import com.community.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("article_author");
        user.setPassword("encoded-pwd");
        user.setNickname("作者");
        userRepository.save(user);

        var auth = new UsernamePasswordAuthenticationToken(
                user.getUsername(), null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void create_shouldReturnArticle() {
        ArticleRequest request = new ArticleRequest();
        request.setTitle("我的第一篇文章");
        request.setContent("这是一段测试内容。");

        ArticleResponse resp = articleService.create(request);

        assertNotNull(resp.getId());
        assertEquals("我的第一篇文章", resp.getTitle());
        assertEquals(user.getId(), resp.getAuthorId());
        assertEquals(0, resp.getCommentCount());
    }

    @Test
    void list_shouldReturnPagedResult() {
        ArticleRequest request = new ArticleRequest();
        request.setTitle("标题");
        request.setContent("内容");
        articleService.create(request);

        var page = articleService.list(1, 10);

        assertTrue(page.getTotalElements() >= 1);
        assertEquals("标题", page.getContent().get(0).getTitle());
    }
}
