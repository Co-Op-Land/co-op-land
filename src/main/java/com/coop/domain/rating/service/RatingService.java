package com.coop.domain.rating.service;

import com.coop.domain.member.entity.Member;
import com.coop.domain.member.service.MemberComponent;
import com.coop.domain.playHistory.entity.History;
import com.coop.domain.playHistory.service.HistoryComponent;
import com.coop.domain.rating.entity.Rating;
import com.coop.domain.rating.repository.RatingRepository;
import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.NotFoundException;
import com.coop.presentation.rating.dto.request.RatingRequest;
import com.coop.presentation.rating.dto.response.RatingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final MemberComponent memberComponent;
    private final HistoryComponent historyComponent;

    @Transactional
    public Rating generateReview(RatingRequest request, Long memberId) {
        Set<Long> memberIds = new HashSet<>(Arrays.asList(memberId, request.toMemberId()));

        List<Member> members = memberComponent.getMembers(memberIds);
        Member fromMember = getMember(memberId, members);
        Member toMember = getMember(request.toMemberId(), members);

        History history = historyComponent.findById(request.historyId());

        Rating rating = request.toEntity(fromMember, history, toMember);
        toMember.updateRating(findRatingAverage(toMember.getId()));
        return ratingRepository.save(rating);
    }

    private static Member getMember(Long memberId, List<Member> members) {
        return members.stream()
                .filter(member -> member.getId().equals(memberId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public List<RatingResponse> findReviews(Long memberId) {
        Member member = getMember(memberId);
        List<Rating> ratings = ratingRepository.findByToMember(member);
        return ratings.stream().map(RatingResponse::from).toList();
    }

    private double findRatingAverage(Long memberId) {
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
