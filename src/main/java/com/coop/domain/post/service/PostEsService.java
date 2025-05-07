package com.coop.domain.post.service;

import com.coop.domain.member.entity.Member;
import com.coop.domain.post.entity.Post;
import com.coop.domain.post.entity.PostDocument;
import com.coop.domain.post.repository.PostEsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostEsService {

    private final PostEsRepository postEsRepository;

    @Transactional
    public void generatePostDocument(Post post, Member member) {
        PostDocument document = PostDocument.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contentPreview(generatePreview(post.getContent()))
                .author(member.getNickname())
                .category(post.getCategory().toString())
                .updatedAt(post.getUpdatedAt())
                .build();

        postEsRepository.save(document);
    }

    private String generatePreview(String content) {
        int previewLength = 50;
        return content.length() > previewLength
                ? content.substring(0, previewLength) + "..."
                : content;
    }
}
