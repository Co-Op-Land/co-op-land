package com.coop.domain.rating.service;

import com.coop.domain.member.entity.Member;
import com.coop.domain.member.service.MemberComponent;
import com.coop.domain.rating.entity.Rating;
import com.coop.domain.rating.repository.RatingRepository;
import com.coop.presentation.rating.dto.request.RatingRequest;
import com.coop.presentation.rating.dto.response.RatingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final MemberComponent memberComponent;

    public Rating generateReview(RatingRequest request, Long memberId) {
        Member member = getMember(memberId);
        Rating rating = request.toEntity(member);
        //member에 rating 값 갱신
        return ratingRepository.save(rating);
    }

    public List<RatingResponse> findReviews(Long memberId) {
        Member member = getMember(memberId);
        List<Rating> ratings = ratingRepository.findByToMember(member);
        return ratings.stream().map(RatingResponse::from).toList();
    }

    public double findRatingAverage(Long memberId) {
        Member member = getMember(memberId);
        List<Rating> reviews = ratingRepository.findByToMember(member);
        return reviews.stream()
                .mapToInt(Rating::getScore)
                .average()
                .orElse(0.0);
    }

    public void removeRating(Long id) {
        ratingRepository.deleteById(id);
    }


    private Member getMember(Long memberId) {
        return memberComponent.findById(memberId);
    }
}
