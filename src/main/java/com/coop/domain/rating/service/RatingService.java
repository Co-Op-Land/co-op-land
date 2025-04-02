package com.coop.domain.rating.service;

import com.coop.domain.member.entity.Member;
import com.coop.domain.member.repository.MemberRepository;
import com.coop.domain.rating.entity.Rating;
import com.coop.domain.rating.repository.RatingRepository;
import com.coop.global.exception.error.NotFoundException;
import com.coop.presentation.rating.dto.request.RatingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final MemberRepository memberRepository;

    public Rating generateReview(RatingRequest request, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundException::new);
        Rating rating = request.toEntity(member);
        return ratingRepository.save(rating);
    }

    public List<Rating> findReviews(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundException::new);
        return ratingRepository.findByToMember(member);
    }

    public double findRatingAverage(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundException::new);
        List<Rating> reviews = ratingRepository.findByToMember(member);
        return reviews.stream()
                .mapToInt(Rating::getScore)
                .average()
                .orElse(0.0);
    }

    public void removeRating(Long id) {
        ratingRepository.deleteById(id);
    }
}
