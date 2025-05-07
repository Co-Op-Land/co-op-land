package com.coop.domain.post.repository;

import com.coop.domain.post.entity.PostDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PostEsRepository extends ElasticsearchRepository<PostDocument, Long> {

}
