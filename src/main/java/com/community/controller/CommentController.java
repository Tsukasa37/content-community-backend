package com.community.controller;

import com.community.dto.ApiResponse;
import com.community.dto.CommentRequest;
import com.community.dto.CommentResponse;
import com.community.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/article/{articleId}")
    public ApiResponse<CommentResponse> create(@PathVariable Long articleId,
                                               @Valid @RequestBody CommentRequest request) {
        return ApiResponse.ok(commentService.create(articleId, request));
    }

    @GetMapping("/article/{articleId}")
    public ApiResponse<List<CommentResponse>> listByArticle(@PathVariable Long articleId) {
        return ApiResponse.ok(commentService.listByArticle(articleId));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        commentService.delete(id);
        return ApiResponse.ok();
    }
}
