package com.coop.global.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.analysis.NoriDecompoundMode;
import co.elastic.clients.elasticsearch._types.analysis.TokenFilterDefinition;
import co.elastic.clients.elasticsearch._types.analysis.TokenizerDefinition;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ElasticsearchIndexConfig {

    private final ElasticsearchClient elasticsearchClient;

    @PostConstruct
    public void setupIndices() {
        try {
            // 인덱스 존재 여부 확인
            boolean exists = elasticsearchClient.indices()
                    .exists(e -> e.index("posts"))
                    .value();

            if (!exists) {
                // 인덱스 생성
                elasticsearchClient.indices().create(c -> c
                        .index("posts")
                        .settings(s -> s
                                .numberOfReplicas("0")
                                .analysis(a -> a
                                        // 토크나이저 설정
                                        .tokenizer("nori_tokenizer", t -> t
                                                .definition(TokenizerDefinition.of(td -> td
                                                        .noriTokenizer(nt -> nt
                                                                .decompoundMode(NoriDecompoundMode.Mixed)
                                                                .userDictionary("user_dict.txt")))))
                                        // 품사 필터 - E: 어미, J: 조사, SP~SE: 공백, '(', ')', '.', ',', ';', '?', '!'
                                        .filter("nori_pos_filter", f -> f
                                                .definition(TokenFilterDefinition.of(tfd -> tfd
                                                        .noriPartOfSpeech(npos -> npos
                                                                .stoptags(List.of(
                                                                        "E", "J", "SP", "SSC", "SSO", "SC", "SE"))))))
                                        // 동의어 필터
                                        .filter("synonym_filter", f -> f
                                                .definition(TokenFilterDefinition.of(tfd -> tfd
                                                        .synonym(syn -> syn
                                                                .synonyms("synonyms.txt")))))
                                        // 커스텀 분석기
                                        .analyzer("nori", n -> n
                                                .custom(ca -> ca
                                                        .tokenizer("nori_tokenizer")
                                                        .filter(List.of(
                                                                "nori_pos_filter", "lowercase", "synonym_filter"))))))
                        .mappings(m -> m
                                .properties("title", p -> p.text(t -> t.analyzer("nori")))
                                .properties("contentPreview", p -> p.text(t -> t.analyzer("nori")))
                                .properties("author", p -> p.keyword(k -> k))
                                .properties("category", p -> p.keyword(k -> k))
                                .properties("updatedAt", p -> p.date(d -> d))));
                log.info("'posts' 인덱스가 성공적으로 생성되었습니다.");
            } else {
                log.info("'posts' 인덱스가 이미 존재합니다.");
            }
        } catch (IOException e) {
            throw new RuntimeException("엘라스틱서치 인덱스 생성 실패: " + e.getMessage(), e);
        }
    }
}
