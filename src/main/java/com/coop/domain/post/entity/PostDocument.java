package com.coop.domain.post.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Document(indexName = "posts", createIndex = false)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostDocument {
    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String title;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String contentPreview;

    @Field(type = FieldType.Keyword)
    private String author;

    @Field(type = FieldType.Keyword)
    private String category;

    @Field(type = FieldType.Date)
    private LocalDateTime updatedAt;

    @Builder
    public PostDocument(
            Long id,
            String title,
            String contentPreview,
            String author,
            String category,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.title = title;
        this.contentPreview = contentPreview;
        this.author = author;
        this.category = category;
        this.updatedAt = updatedAt;
    }
}
