package com.coop.presentation.post.controller;

import com.coop.domain.post.service.PostService;
import com.coop.global.common.ApiResponse;
import com.coop.global.security.AuthUser;
import com.coop.presentation.post.dto.request.PostRequest;
import com.coop.presentation.post.dto.response.PostPageResponse;
import com.coop.presentation.post.dto.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createPost(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody PostRequest request
    ) {
        Long id = postService.generatePost(authUser.memberId(), request);
        return ApiResponse.created(id);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PostPageResponse>> readPages(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String category
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        PostPageResponse responseDto = postService.getPosts(pageable, category);

        return ApiResponse.success(responseDto);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> readPage(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").ascending());
        PostResponse responseDto = postService.getPost(postId, pageable);

        return ApiResponse.success(responseDto);
    }


    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> updatePost(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long postId,
            @RequestBody PostRequest request
    ) {
        postService.modifyPost(authUser.memberId(), postId, request);
        return ApiResponse.noContent();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long postId
    ) {
        postService.removePost(authUser.memberId(), postId);
        return ApiResponse.noContent();
    }
}
