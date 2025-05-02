package com.coop.presentation.post.dto.response;

import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record PostDocPageResponse(List<PostDocsResponse> postDocs, int currentPage, int totalPage, boolean hasNext) {
    public static PostDocPageResponse from(Page<PostDocsResponse> pageResponse) {
        return PostDocPageResponse.builder()
                .postDocs(pageResponse.getContent())
                .currentPage(pageResponse.getNumber())
                .totalPage(pageResponse.getTotalPages())
                .hasNext(pageResponse.hasNext())
                .build();
    }
}
