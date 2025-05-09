package com.coop.domain.post.repository;

import com.coop.domain.post.entity.PostDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface PostSearchRepository extends ElasticsearchRepository<PostDocument, Long> {

    List<PostDocument> findByTitleContainingOrContentPreviewContaining(String title, String content);
    List<PostDocument> findByTitleContaining(String title);
    List<PostDocument> findByContentPreviewContaining(String content);
}
