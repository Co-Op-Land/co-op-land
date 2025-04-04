package com.coop.presentation.comment.controller;

import com.coop.domain.comment.service.CommentService;
import com.coop.global.common.ApiResponse;
import com.coop.presentation.comment.dto.request.CommentRequest;
import com.coop.presentation.comment.dto.response.CommentPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<Long>> createComment(
            @AuthenticationPrincipal User userDetails,
            @PathVariable Long postId,
            @RequestBody CommentRequest request
    ) {
        Long id = commentService.generateComment(userDetails, postId, request);
        return ApiResponse.created(id);
    }

    @GetMapping("/members/{memberId}/comments")
    public ResponseEntity<ApiResponse<CommentPageResponse>> readMemberComments(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        return ApiResponse.success(commentService.getMemberComments(memberId, pageable));
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<CommentPageResponse>> readPostComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").ascending());
        return ApiResponse.success(commentService.getPostComments(postId, pageable));
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<Void>> updateComment(
            @AuthenticationPrincipal User userDetails,
            @PathVariable Long commentId,
            @RequestBody CommentRequest request
    ) {
        commentService.modifyComment(userDetails, commentId, request);
        return ApiResponse.noContent();
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @AuthenticationPrincipal User userDetails,
            @PathVariable Long commentId
    ) {
        commentService.removeComment(userDetails, commentId);
        return ApiResponse.noContent();
    }
}
