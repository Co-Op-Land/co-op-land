package com.coop.global.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import java.time.Duration;

@Configuration
public class ElasticSearchConfig extends ElasticsearchConfiguration {

    @Value("${spring.elasticsearch.uris}")
    private String elasticsearchUrl;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(elasticsearchUrl)
                .withConnectTimeout(Duration.ofSeconds(5))
                .withSocketTimeout(Duration.ofSeconds(30))
                .build();
    }

    @Bean
    public ElasticsearchOperations elasticsearchOperations(ElasticsearchClient elasticsearchClient) {
        return new ElasticsearchTemplate(elasticsearchClient);
    }
}