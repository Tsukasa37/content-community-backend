package com.community.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentResponse {

    private Long id;
    private String content;
    private Long articleId;
    private Long authorId;
    private String authorName;
    private LocalDateTime createdAt;
}
