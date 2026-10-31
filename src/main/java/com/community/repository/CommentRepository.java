package com.community.repository;

import com.community.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByArticleIdOrderByCreatedAtAsc(Long articleId);

    long countByArticleId(Long articleId);
}
