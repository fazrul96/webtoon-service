package com.webtoon_service.infrastructure.storage;

import com.webtoon_service.domain.dto.response.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.PathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.util.List;

import static com.webtoon_service.constants.ApiConstant.WEBTOON.UPLOAD_FILES_S3;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3StorageClient {
    private final WebClient storageWebClient;

    private MultiValueMap<String, HttpEntity<?>> buildMultipartRequest(Path zipFile) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("files", new PathResource(zipFile));
        return builder.build();
    }

    public Mono<List<String>> uploadFiles(Path zipFile, String prefix) {
        MultiValueMap<String, HttpEntity<?>> multipartBody = buildMultipartRequest(zipFile);

        return storageWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(UPLOAD_FILES_S3)
                        .queryParam("prefix", prefix)
                        .build())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(multipartBody)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponseDto<List<String>>>() {})
                .map(ApiResponseDto::getData)
                .doOnNext(response -> log.info("✅ Uploaded file(s) to storage: {}", response))
                .doOnError(err -> log.error("❌ Failed to upload files to storage", err));
    }
}
