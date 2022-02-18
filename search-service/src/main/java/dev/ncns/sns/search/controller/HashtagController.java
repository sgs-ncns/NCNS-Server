package dev.ncns.sns.search.controller;

import dev.ncns.sns.common.controller.ApiController;
import dev.ncns.sns.common.domain.ResponseEntity;
import dev.ncns.sns.search.dto.request.CreateHashtagRequestDto;
import dev.ncns.sns.search.dto.request.UpdateHashtagRequestDto;
import dev.ncns.sns.search.dto.response.HashtagResponseDto;
import dev.ncns.sns.search.service.HashtagService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/api/search/hashtag")
@RestController
public class HashtagController extends ApiController {

    private final HashtagService hashtagService;

    @Operation(summary = "[Only Server] 해시태그 조회")
    @GetMapping
    public ResponseEntity<HashtagResponseDto> getHashtag(@RequestParam String content) {
        HashtagResponseDto hashtagResponse = hashtagService.getHashtag(content);
        return getSuccessResponse(hashtagResponse);
    }

    @Operation(summary = "[Only Server] 해시태그 등록", description = "게시물 ID 추가(status:true) / 제거(status:false)")
    @PostMapping
    public ResponseEntity<Void> createHashtag(@RequestBody CreateHashtagRequestDto createHashtagRequest) {
        hashtagService.createHashtag(createHashtagRequest);
        return getSuccessResponse();
    }

    @Operation(summary = "[Only Server] 해시태그 수정")
    @PutMapping
    public ResponseEntity<Void> updateHashtag(@RequestBody UpdateHashtagRequestDto updateHashtagRequest) {
        hashtagService.updateHashtag(updateHashtagRequest);
        return getSuccessResponse();
    }

    @Operation(summary = "[Only Server] 해시태그 삭제")
    @DeleteMapping
    public ResponseEntity<Void> deleteHashtag(@RequestParam String content) {
        hashtagService.deleteHashtag(content);
        return getSuccessResponse();
    }

    @Operation(summary = "[Only Server] 내용으로 해시태그 조회", description = "키워드가 포함된 모든 해시태그 조회")
    @GetMapping("/content")
    public ResponseEntity<List<HashtagResponseDto>> findHashtagsByContent(@RequestParam String keyword) {
        List<HashtagResponseDto> hashtagResponse = hashtagService.findHashtagsByContent(keyword);
        return getSuccessResponse(hashtagResponse);
    }

}
