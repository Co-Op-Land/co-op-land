package com.coop.domain.rating.service;

import com.coop.domain.member.entity.Member;
import com.coop.domain.member.service.MemberComponent;
import com.coop.domain.notification.enums.NotificationTarget;
import com.coop.domain.playHistory.entity.History;
import com.coop.domain.playHistory.service.HistoryComponent;
import com.coop.domain.rating.entity.Rating;
import com.coop.domain.rating.repository.RatingRepository;
import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.EntityAlreadyExistException;
import com.coop.global.exception.error.NotFoundException;
import com.coop.global.notification.annotation.TriggerNotification;
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
    @TriggerNotification(target = NotificationTarget.RATING)
    public RatingResponse generateReview(RatingRequest request, Long memberId) {
        Set<Long> memberIds = new HashSet<>(Arrays.asList(memberId, request.toMemberId()));

        List<Member> members = memberComponent.getMembers(memberIds);
        checkRatingDuplicated(request, memberId);
        Member fromMember = getMember(memberId, members);
        Member toMember = getMember(request.toMemberId(), members);

        History history = historyComponent.findById(request.historyId());

        Rating rating = request.toEntity(fromMember, history, toMember);
        toMember.updateRating(findRatingAverage(toMember.getId()));
        Rating savedRating = ratingRepository.save(rating);
        return RatingResponse.from(savedRating);
    }

    private void checkRatingDuplicated(RatingRequest request, Long memberId) {
        boolean exists = ratingRepository.existsByHistoryIdAndFromMemberIdAndToMemberId(
                request.historyId(),
                memberId,
                request.toMemberId()
        );

        if (exists) {
            throw new EntityAlreadyExistException(ErrorCode.RATING_ALREADY_EXIST);
        }
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
