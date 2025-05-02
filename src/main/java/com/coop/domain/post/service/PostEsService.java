package com.coop.domain.post.service;

import com.coop.domain.member.entity.Member;
import com.coop.domain.post.entity.Post;
import com.coop.domain.post.entity.PostDocument;
import com.coop.domain.post.event.PostCreatedEvent;
import com.coop.domain.post.repository.PostEsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostEsService {

    private final PostEsRepository postEsRepository;

    @Transactional
    public void generatePostDocument(Post post, Member member) {
        PostDocument document = PostDocument.of(post, member);
        postEsRepository.save(document);
    }

    @Transactional
    public void saveAllFromEvents(List<PostCreatedEvent> events) {
        List<PostDocument> documents = events.stream()
                .map(event -> PostDocument.of(event.getPost(), event.getMember()))
                .toList();

        postEsRepository.saveAll(documents);
    }
}
