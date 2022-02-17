package dev.ncns.sns.search.controller;

import dev.ncns.sns.common.controller.ApiController;
import dev.ncns.sns.common.domain.ResponseEntity;
import dev.ncns.sns.search.dto.request.CreateHashtagRequestDto;
import dev.ncns.sns.search.dto.response.HashtagResponseDto;
import dev.ncns.sns.search.service.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping(value = "/api/search/hashtag")
@RestController
public class HashtagController extends ApiController {

    private final HashtagService hashtagService;

    @GetMapping
    public ResponseEntity<HashtagResponseDto> getHashtag(@RequestParam String content) {
        HashtagResponseDto hashtagResponse = hashtagService.getHashtag(content);
        return getSuccessResponse(hashtagResponse);
    }

    @PostMapping
    public ResponseEntity<Void> createHashtag(@RequestBody CreateHashtagRequestDto createHashtagRequest) {
        hashtagService.createHashtag(createHashtagRequest);
        return getSuccessResponse();
    }

}
