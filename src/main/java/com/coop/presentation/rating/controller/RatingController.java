package com.coop.presentation.rating.controller;

import com.coop.domain.rating.service.RatingService;
import com.coop.global.common.ApiResponse;
import com.coop.global.security.AuthUser;
import com.coop.presentation.rating.dto.request.RatingRequest;
import com.coop.presentation.rating.dto.response.RatingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping
    public ResponseEntity<ApiResponse<RatingResponse>> createRating(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody RatingRequest request
            ) {
        RatingResponse rating = ratingService.generateReview(request, authUser.memberId());
        return ApiResponse.created(rating);
    }

    @GetMapping("/review/{id}")
    public ResponseEntity<ApiResponse<List<RatingResponse>>> readMemberReviews(
            @PathVariable Long id
    ) {
        return ApiResponse.success(ratingService.findReviews(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRating(
            @PathVariable Long id
    ) {
        ratingService.removeRating(id);
        return ApiResponse.noContent();
    }
}
