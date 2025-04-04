package com.coop.domain.post.service;

import com.coop.domain.comment.entity.Comment;
import com.coop.domain.comment.repository.CommentRepository;
import com.coop.domain.member.entity.Member;
import com.coop.domain.member.repository.MemberRepository;
import com.coop.domain.post.entity.Post;
import com.coop.domain.post.enums.PostCategory;
import com.coop.domain.post.repository.PostRepository;
import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.AccessDeniedException;
import com.coop.global.exception.error.NotFoundException;
import com.coop.presentation.comment.dto.response.CommentsResponse;
import com.coop.presentation.post.dto.request.PostRequest;
import com.coop.presentation.post.dto.response.PostPageResponse;
import com.coop.presentation.post.dto.response.PostResponse;
import com.coop.presentation.post.dto.response.PostsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Long generatePost(UserDetails userDetails, PostRequest request) {
        Member member = memberRepository.getReferenceById(Long.valueOf(userDetails.getUsername()));
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
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_POST));
        Page<Comment> comments = commentRepository.findAllByPost_Id(postId, pageable);
        Page<CommentsResponse> commentPage = comments.map(CommentsResponse::of);

        return PostResponse.from(post, commentPage);
    }

    @Transactional
    public void modifyPost(UserDetails userDetails, Long postId, PostRequest request) {
        Post post = getPostById(postId);
        validateAccess(post, userDetails);

        post.modify(request.title(), request.content(), PostCategory.of(request.category()));
    }

    @Transactional
    public void removePost(UserDetails userDetails, Long postId) {
        Post post = getPostById(postId);
        validateAccess(post, userDetails);

        postRepository.delete(post);
    }

    private Post getPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_POST));
    }

    private void validateAccess(Post post, UserDetails userDetails) {
        if (!post.getMember().getId().equals(Long.valueOf(userDetails.getUsername()))) {
            throw new AccessDeniedException();
        }
    }
}
