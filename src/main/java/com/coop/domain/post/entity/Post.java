package com.coop.domain.post.entity;

import com.coop.domain.member.entity.Member;
import com.coop.domain.post.enums.PostCategory;
import com.coop.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private PostCategory category;

    @Builder
    private Post(
            Member member,
            String title,
            String content,
            PostCategory category
    ) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public void modify(String title, String content, PostCategory category) {
        modifyTitle(title);
        modifyContent(content);
        modifyCategory(category);
    }

    private void modifyTitle(String newTitle) {
        if (newTitle != null && !newTitle.isBlank()) {
            this.title = newTitle;
        }
    }

    private void modifyContent(String newContent) {
        if (newContent != null && !newContent.isBlank()) {
            this.content = newContent;
        }
    }

    private void modifyCategory(PostCategory newCategory) {
        if (newCategory != null) {
            this.category = newCategory;
        }
    }
}
