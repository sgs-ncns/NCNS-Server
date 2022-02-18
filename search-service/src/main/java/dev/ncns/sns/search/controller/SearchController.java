package dev.ncns.sns.search.controller;

import dev.ncns.sns.common.controller.ApiController;
import dev.ncns.sns.common.domain.ResponseEntity;
import dev.ncns.sns.search.dto.response.HashtagResponseDto;
import dev.ncns.sns.search.dto.response.SearchResponseDto;
import dev.ncns.sns.search.dto.response.UserResponseDto;
import dev.ncns.sns.search.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/api/search/type")
@RestController
public class SearchController extends ApiController {

    private final SearchService searchService;

    @Operation(summary = "전체 검색", description = "사용자 계정이름 or 닉네임 + 해시태그 내용으로 검색")
    @GetMapping("/all")
    public ResponseEntity<SearchResponseDto> searchAll(@RequestParam String keyword) {
        SearchResponseDto searchResponse = searchService.searchAll(keyword);
        return getSuccessResponse(searchResponse);
    }

    @Operation(summary = "사용자 검색", description = "사용자 계정이름 or 닉네임으로 검색")
    @GetMapping("/user")
    public ResponseEntity<List<UserResponseDto>> searchUsers(@RequestParam String keyword,
                                                             @RequestParam int page) {
        List<UserResponseDto> userResponse = searchService.searchUserByName(keyword, page);
        return getSuccessResponse(userResponse);
    }

    @Operation(summary = "해시태그 검색", description = "해시태그 내용으로 검색")
    @GetMapping("/hashtag")
    public ResponseEntity<List<HashtagResponseDto>> searchHashtags(@RequestParam String keyword,
                                                                   @RequestParam int page) {
        List<HashtagResponseDto> hashtagResponse = searchService.searchHashtagByContent(keyword, page);
        return getSuccessResponse(hashtagResponse);
    }

}
