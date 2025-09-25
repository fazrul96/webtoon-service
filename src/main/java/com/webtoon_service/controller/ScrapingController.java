package com.webtoon_service.controller;

import com.webtoon_service.constants.ApiConstant;
import com.webtoon_service.domain.dto.request.WebtoonFetchSuffixRequestDto;
import com.webtoon_service.domain.dto.request.WebtoonUploadChapterRequestDto;
import com.webtoon_service.domain.dto.response.ApiResponseDto;
import com.webtoon_service.service.WebtoonScrapingService;
import com.webtoon_service.service.WebtoonSuffixResolverDispatcherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.webtoon_service.constants.MessageConstants.HttpCodes.*;
import static com.webtoon_service.constants.MessageConstants.HttpDescription.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "${app.publicApiPath}")
@CrossOrigin(origins = "${app.basePath}")
@Tag(name = "Webtoon Scraping", description = "Endpoints for scraping and syncing webtoon data from external providers")
public class ScrapingController extends BaseController {
    private final WebtoonScrapingService webtoonScrapingService;
    private final WebtoonSuffixResolverDispatcherService webtoonSuffixResolverDispatcherService;

    @Value("${webtoon.scraping.headless}")
    private boolean headlessMode;

    @Operation(summary = "Download and Upload the webtoon chapter to S3")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = OK_DESC),
            @ApiResponse(responseCode = BAD_REQUEST, description = BAD_REQUEST_DESC),
            @ApiResponse(responseCode = NOT_FOUND, description = NOT_FOUND_DESC),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR, description = INTERNAL_ERROR_DESC)
    })
    @PostMapping(path = ApiConstant.WEBTOON.UPLOAD_WEBTOON_CHAPTER)
    public ResponseEntity<ApiResponseDto<List<String>>> uploadWebtoonChapter(
            @RequestBody WebtoonUploadChapterRequestDto request
    ) {
        List<String> response = webtoonScrapingService.uploadWebtoonChapter(
                request.getProvider(),
                request.getTitle(),
                request.getChapter(),
                headlessMode
        );

        return apiSuccess("Upload successful", response);
    }

    @Operation(summary = "Fetch webtoon suffix from a given provider")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = OK_DESC),
            @ApiResponse(responseCode = BAD_REQUEST, description = BAD_REQUEST_DESC),
            @ApiResponse(responseCode = NOT_FOUND, description = NOT_FOUND_DESC),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR, description = INTERNAL_ERROR_DESC)
    })
    @PostMapping(path = ApiConstant.WEBTOON.FETCH_SUFFIX)
    public ResponseEntity<ApiResponseDto<Map<String, String> >> fetchSuffix(
            @RequestBody WebtoonFetchSuffixRequestDto request
    ) {
        Map<String, String> response = webtoonSuffixResolverDispatcherService.resolve(
                request.getProvider(),
                request.getTitle(),
                headlessMode
        );

        return apiSuccess("Fetch suffix successful", response);
    }
}
