package com.coop.domain.post.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Document(indexName = "posts")
public class PostDocument {

    @Id
    private Long id;
    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;
    @Field(type = FieldType.Text, analyzer = "standard")
    private String content;

    public PostDocument() {
    }

    @Builder
    public PostDocument(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
