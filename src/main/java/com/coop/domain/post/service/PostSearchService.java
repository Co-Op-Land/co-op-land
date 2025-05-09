package com.coop.domain.post.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.coop.domain.post.entity.Post;
import com.coop.domain.post.entity.PostDocument;
import com.coop.domain.post.enums.PostCategory;
import com.coop.domain.post.repository.PostRepository;
import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.ElasticsearchException;
import com.coop.presentation.post.dto.response.*;
import com.coop.domain.post.repository.PostSearchRepository;
import com.coop.presentation.post.dto.response.PostSearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostSearchService {

    private final PostRepository postRepository;
    private final ElasticsearchClient elasticsearchClient;

    public PostDocPageResponse getPostDocsBySearching(
            Pageable pageable, String title, String content, String author, String category, String updatedAt
    ) {
        Page<PostDocument> postDocuments = searchByFields
                (pageable, title, content, author, category, updatedAt != null ? LocalDateTime.parse(updatedAt) : null);
        Page<PostDocsResponse> postPage = postDocuments.map(PostDocsResponse::of);

        return PostDocPageResponse.from(postPage);
    }

    private Page<PostDocument> searchByFields(
            Pageable pageable, String title, String content, String author, String category, LocalDateTime updatedAt
    ) {
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

        if (StringUtils.hasText(title)) {
            boolQueryBuilder.must(
                    q -> q.match(m -> m.field("title").query(title)));
        }

        if (StringUtils.hasText(content)) {
            boolQueryBuilder.must(
                    q -> q.match(m -> m.field("contentPreview").query(content)));
        }

        if (StringUtils.hasText(author)) {
            boolQueryBuilder.must(
                    q -> q.term(m -> m.field("author").value(author)));
        }

        if (StringUtils.hasText(category)) {
            PostCategory.isValid(category);
            boolQueryBuilder.must(
                    q -> q.term(m -> m.field("category").value(category)));
        }

        if (updatedAt != null) {
            boolQueryBuilder.must(
                    q -> q.range(m -> m.field("updatedAt").gte(JsonData.of(updatedAt))));
        }

        try {
            SearchResponse<PostDocument> response = elasticsearchClient.search(s -> s
                    .index("posts")
                    .from((int) pageable.getOffset())
                    .size(pageable.getPageSize())
                    .query(q -> q.bool(boolQueryBuilder.build())),
                    PostDocument.class);

            List<PostDocument> documents = response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();

            long totalHits = Objects.requireNonNull(response.hits().total()).value();

            return new PageImpl<>(documents, pageable, totalHits);

        } catch (Exception e) {
            log.error("엘라스틱서치 검색 오류", e);
            throw new ElasticsearchException(ErrorCode.ES_SEARCH_ERROR);
        }
    }
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
        return postSearchRepository.findByTitleContainingOrContentPreviewContaining(keyword, keyword);
    }
}
