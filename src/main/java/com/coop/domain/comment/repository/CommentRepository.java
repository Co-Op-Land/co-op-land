package com.coop.domain.comment.repository;

import com.coop.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByMember_Id(Long memberId, Pageable pageable);
    Page<Comment> findAllByPost_Id(Long postId, Pageable pageable);
    Page<Comment> findAllByPost_IdAndParentIsNull(Long postId, Pageable pageable);
}
