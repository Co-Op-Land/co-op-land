package com.coop.presentation.post.controller;

import com.coop.domain.post.enums.PostCategory;
import com.coop.domain.post.service.PostEsService;
import com.coop.domain.post.service.PostSearchService;
import com.coop.global.common.ApiResponse;
import com.coop.presentation.post.dto.response.PostDocPageResponse;
import com.coop.presentation.post.dto.response.PostSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class PostSearchController {

    private final PostSearchService postSearchService;
    private final PostEsService postEsService;

    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<PostDocPageResponse>> readPostsBySearching(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String updatedAt
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("updatedAt").descending());
        PostDocPageResponse responseDto =
                postSearchService.getPostDocsBySearching(pageable, title, content, author, category, updatedAt);

        return ApiResponse.success(responseDto);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostSearchResponse>>> readPostBySearch(
            @RequestParam String keyword
    ) {
        return ApiResponse.success(postSearchService.findPostByKeyword(keyword));
    }

    @GetMapping("/fulltext-index/boolean-mode")
    public ResponseEntity<ApiResponse<List<PostSearchResponse>>> readPostBySearchFromKeywordByBoolean(
            @RequestParam String keyword
    ) {
        return ApiResponse.success(postSearchService.searchPostsByBooleanMode(keyword));
    }

    @GetMapping("/fulltext-index/natural-language-mode")
    public ResponseEntity<ApiResponse<List<PostSearchResponse>>> readPostBySearchFromKeywordByNaturalLanguage(
            @RequestParam String keyword
    ) {
        return ApiResponse.success(postSearchService.searchPostsByNaturalLanguageMode(keyword));
    }

    @GetMapping("/fi/category")
    public ResponseEntity<ApiResponse<List<PostSearchResponse>>> readPostBySearchFromKeywordAndCategory(
            @RequestParam String keyword,
            @RequestParam PostCategory category
    ) {
        return ApiResponse.success(postSearchService.searchPostsByCategory(keyword, category));
    }
}
