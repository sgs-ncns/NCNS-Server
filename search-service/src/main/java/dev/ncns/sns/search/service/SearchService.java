package dev.ncns.sns.search.service;

import dev.ncns.sns.search.domain.Hashtag;
import dev.ncns.sns.search.domain.User;
import dev.ncns.sns.search.dto.response.HashtagResponseDto;
import dev.ncns.sns.search.dto.response.SearchResponseDto;
import dev.ncns.sns.search.dto.response.UserResponseDto;
import dev.ncns.sns.search.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SearchService {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;

    private final SearchRepository searchRepository;

    public SearchResponseDto searchAll(String keyword) {
        List<UserResponseDto> users = searchUserByName(keyword, DEFAULT_PAGE);
        List<HashtagResponseDto> hashtags = searchHashtagByContent(keyword, DEFAULT_PAGE);
        return SearchResponseDto.of(users, hashtags);
    }

    public List<UserResponseDto> searchUserByName(String keyword, int page) {
        List<User> users = searchRepository.searchUsers(keyword, getPageable(page));
        return users.stream()
                .map(UserResponseDto::of)
                .collect(Collectors.toList());
    }

    public List<HashtagResponseDto> searchHashtagByContent(String keyword, int page) {
        List<Hashtag> hashtags = searchRepository.searchHashtags(keyword, getPageable(page));
        return hashtags.stream()
                .map(HashtagResponseDto::of)
                .collect(Collectors.toList());
    }

    private Pageable getPageable(int page) {
        return PageRequest.of(page, DEFAULT_SIZE);
    }

}
