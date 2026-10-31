package com.community.controller;

import com.community.dto.ApiResponse;
import com.community.dto.ArticleRequest;
import com.community.dto.ArticleResponse;
import com.community.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    public ApiResponse<ArticleResponse> create(@Valid @RequestBody ArticleRequest request) {
        return ApiResponse.ok(articleService.create(request));
    }

    @GetMapping
    public ApiResponse<Page<ArticleResponse>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(articleService.list(page, size));
    }

    @GetMapping("/{id}")
    public ApiResponse<ArticleResponse> getById(@PathVariable Long id) {
        return ApiResponse.ok(articleService.getById(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<ArticleResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody ArticleRequest request) {
        return ApiResponse.ok(articleService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        articleService.delete(id);
        return ApiResponse.ok();
    }
}
