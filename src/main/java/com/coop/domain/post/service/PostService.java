package com.coop.domain.post.service;

import com.coop.domain.comment.entity.Comment;
import com.coop.domain.comment.service.CommentService;
import com.coop.domain.member.entity.Member;
import com.coop.domain.member.service.MemberComponent;
import com.coop.domain.post.entity.Post;
import com.coop.domain.post.entity.PostDocument;
import com.coop.domain.post.enums.PostCategory;
import com.coop.domain.post.event.PostCreatedEvent;
import com.coop.domain.post.repository.PostRepository;
import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.AccessDeniedException;
import com.coop.global.exception.error.ElasticsearchException;
import com.coop.global.exception.error.NotFoundException;
import com.coop.presentation.comment.dto.response.PostCommentsResponse;
import com.coop.presentation.post.dto.request.PostRequest;
import com.coop.presentation.post.dto.response.PostPageResponse;
import com.coop.presentation.post.dto.response.PostResponse;
import com.coop.presentation.post.dto.response.PostsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final MemberComponent memberComponent;
    private final CommentService commentService;
    private final ApplicationEventPublisher eventPublisher;
    private final PostDocumentMapper documentMapper;

    @Transactional
    public Long generatePost(Long memberId, PostRequest request) {
        Member member = memberComponent.findById(memberId);
        Post post = postRepository.save(request.of(member));
        PostDocument document = documentMapper.toDocument(post, member);
        try {
            eventPublisher.publishEvent(document);
        } catch (Exception e) {
            log.error("post: 이벤트 발행 실패 - {}", post.getId(), e);
            throw new ElasticsearchException(ErrorCode.ES_EVENT_ERROR);
        }
        return post.getId();
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
