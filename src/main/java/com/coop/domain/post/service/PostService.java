package com.coop.domain.post.service;

import com.coop.domain.comment.entity.Comment;
import com.coop.domain.comment.service.CommentService;
import com.coop.domain.member.entity.Member;
import com.coop.domain.member.service.MemberComponent;
import com.coop.domain.post.entity.Post;
import com.coop.domain.post.enums.PostCategory;
import com.coop.domain.post.repository.PostRepository;
import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.AccessDeniedException;
import com.coop.global.exception.error.NotFoundException;
import com.coop.presentation.comment.dto.response.PostCommentsResponse;
import com.coop.presentation.post.dto.request.PostRequest;
import com.coop.presentation.post.dto.response.PostPageResponse;
import com.coop.presentation.post.dto.response.PostResponse;
import com.coop.presentation.post.dto.response.PostsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final MemberComponent memberComponent;
    private final CommentService commentService;

    @Transactional
    public Long generatePost(Long memberId, PostRequest request) {
        Member member = memberComponent.findMemberReference(memberId);
        postRepository.save(request.of(member));

        return member.getId();
    }

    public PostPageResponse getPosts(Pageable pageable, String category) {
        Page<Post> posts = postRepository.findAllByCategory(pageable, PostCategory.of(category));
        Page<PostsResponse> postPage = posts.map(PostsResponse::of);

        return PostPageResponse.from(postPage);
    }

    public PostResponse getPost(Long postId, Pageable pageable) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));
        Page<Comment> comments = commentService.findAllByPost_IdAndParentIsNull(postId, pageable);
        Page<PostCommentsResponse> commentPage = comments.map(PostCommentsResponse::of);

        return PostResponse.from(post, commentPage);
    }

    @Transactional
    public void modifyPost(Long memberId, Long postId, PostRequest request) {
        Post post = getPostById(postId);
        validateAccess(post, memberId);

        post.modify(request.title(), request.content(), PostCategory.of(request.category()));
    }

    @Transactional
    public void removePost(Long memberId, Long postId) {
        Post post = getPostById(postId);
        validateAccess(post, memberId);

        postRepository.delete(post);
    }

    private Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));
    }

    private void validateAccess(Post post, Long memberId) {
        if (!post.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException();
        }
    }
}
