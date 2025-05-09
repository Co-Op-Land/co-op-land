package com.coop.domain.post.service;

import com.coop.domain.member.entity.Member;
import com.coop.domain.post.entity.Post;
import com.coop.domain.post.entity.PostDocument;
import com.coop.domain.post.repository.PostSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostEsService {

    private final PostSearchRepository postSearchRepository;

    @Transactional
    public void saveDocument(PostDocument document) {
        postSearchRepository.save(document);
    }

    @Transactional
    public void saveDocuments(List<PostDocument> documents) {
        postSearchRepository.saveAll(documents);
    }
}
