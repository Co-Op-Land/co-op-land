package com.coop.domain.comment.service;

import com.coop.domain.comment.entity.Comment;
import com.coop.domain.comment.repository.CommentRepository;
import com.coop.domain.member.entity.Member;
import com.coop.domain.member.service.MemberComponent;
import com.coop.domain.post.entity.Post;
import com.coop.domain.post.service.PostComponent;
import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.AccessDeniedException;
import com.coop.global.exception.error.InvalidRequestException;
import com.coop.global.exception.error.NotFoundException;
import com.coop.presentation.comment.dto.request.CommentRequest;
import com.coop.presentation.comment.dto.response.CommentPageResponse;
import com.coop.presentation.comment.dto.response.MemberCommentResponse;
import com.coop.presentation.comment.dto.response.PostCommentsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberComponent memberComponent;
    private final PostComponent postComponent;

    @Transactional
    public Long generateComment(User userDetails, Long postId, CommentRequest request) {
        Member member = memberComponent.findMemberReference(Long.valueOf(userDetails.getUsername()));
        Post post = postComponent.findById(postId);

        Comment comment = Optional.ofNullable(request.parentId())
                .map(parentId -> findParentComment(parentId, postId))
                .map(parent -> {
                    Comment childComment = request.of(member, post, parent);
                    parent.addChild(childComment);
                    return childComment;
                })
                .orElseGet(() -> request.of(member, post, null));
        commentRepository.save(comment);

        return comment.getId();
    }

    public CommentPageResponse getMemberComments(Long memberId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findAllByMember_Id(memberId, pageable);
        Page<MemberCommentResponse> commentPage = comments.map(MemberCommentResponse::of);

        return CommentPageResponse.from(commentPage);
    }

    public CommentPageResponse getPostComments(Long postId, Pageable pageable) {
        Page<Comment> comments = findAllByPost_IdAndParentIsNull(postId, pageable);
        Page<PostCommentsResponse> commentPage = comments.map(PostCommentsResponse::of);

        return CommentPageResponse.from(commentPage);
    }

    @Transactional
    public void modifyComment(User userDetails, Long commentId, CommentRequest request) {
        Comment comment = getCommentById(commentId);
        validateAccess(comment, userDetails);

        comment.modifyContent(request.content());
    }

    @Transactional
    public void removeComment(User userDetails, Long commentId) {
        Comment comment = getCommentById(commentId);
        validateAccess(comment, userDetails);

        commentRepository.delete(comment);
    }

    private Comment findParentComment(Long parentId, Long postId) {
        Comment parent = commentRepository.findById(parentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_COMMENT));

        if (!parent.getPost().getId().equals(postId)) {
            throw new InvalidRequestException(ErrorCode.INVALID_PARENT_ID);
        }

        return parent;
    }

    private Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_COMMENT));
    }

    private void validateAccess(Comment comment, User userDetails) {
        if (!comment.getMember().getId().equals(Long.valueOf(userDetails.getUsername()))) {
            throw new AccessDeniedException();
        }
    }

    public Page<Comment> findAllByPost_IdAndParentIsNull(Long postId, Pageable pageable) {
        return commentRepository.findAllByPost_IdAndParentIsNull(postId, pageable);
    }
}
