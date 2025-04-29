package com.coop.domain.post.service;

import com.coop.domain.post.entity.Post;
import com.coop.domain.post.entity.PostDocument;
import com.coop.domain.post.enums.PostCategory;
import com.coop.domain.post.repository.PostRepository;
import com.coop.domain.post.repository.PostSearchRepository;
import com.coop.presentation.post.dto.response.PostSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostSearchService {

    private final PostRepository postRepository;
    private final PostSearchRepository postSearchRepository;

    public List<PostSearchResponse> findPostByKeyword(String keyword) {
        return postRepository.findByKeyword(keyword).stream()
                .map(PostSearchResponse::from)
                .toList();
    }

    public List<PostSearchResponse> searchPostsByNaturalLanguageMode(String keyword) {
        return postRepository.searchByKeyword(keyword + "*").stream()
                .map(PostSearchResponse::from)
                .toList();
    }

    public List<PostSearchResponse> searchPostsByBooleanMode(String keyword) {
        return postRepository.searchByKeywordBoolean(keyword + "*").stream()
                .map(PostSearchResponse::from)
                .toList();
    }

    public List<PostSearchResponse> searchPostsByCategory(String keyword, PostCategory category) {
        return postRepository.searchByKeywordAndCategory(
                        keyword + "*", category.name()).stream()
                .map(PostSearchResponse::from)
                .toList();
    }

    public Page<Post> searchPostsWithPaging(String keyword, int page, int size) {
        return postRepository.searchByKeywordWithPaging(
                keyword + "*", PageRequest.of(page, size, Sort.by("id").descending()));
    }

    public List<PostDocument> searchPostByElasticSearch(String keyword) {
        return postSearchRepository.findByTitleContainingOrContentContaining(keyword, keyword);
    }
}
