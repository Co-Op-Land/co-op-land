package com.coop.domain.rating.repository;

import com.coop.domain.member.entity.Member;
import com.coop.domain.rating.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findByToMember(Member member);
    Boolean existsByHistoryIdAndFromMemberIdAndToMemberId(Long historyId, Long fromMemberId, Long toMemberId);
}
