package com.community.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ArticleResponse {

    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private String authorName;
    private long commentCount;
    private LocalDateTime createdAt;
}
