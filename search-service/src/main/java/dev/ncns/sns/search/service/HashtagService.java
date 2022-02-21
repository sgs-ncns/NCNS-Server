package dev.ncns.sns.search.service;

import dev.ncns.sns.common.domain.ResponseType;
import dev.ncns.sns.common.exception.NotFoundException;
import dev.ncns.sns.search.domain.Hashtag;
import dev.ncns.sns.search.dto.request.CreateHashtagRequestDto;
import dev.ncns.sns.search.dto.request.UpdateHashtagRequestDto;
import dev.ncns.sns.search.dto.response.HashtagResponseDto;
import dev.ncns.sns.search.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HashtagService {

    private final HashtagRepository hashtagRepository;

    public HashtagResponseDto getHashtag(String content) {
        Hashtag hashtag = hashtagRepository.getByContent(content)
                .orElseThrow(() -> new NotFoundException(ResponseType.SEARCH_NOT_EXIST_HASHTAG));
        return HashtagResponseDto.of(hashtag);
    }

    public void createHashtag(CreateHashtagRequestDto createHashtagRequest) {
        Hashtag hashtag = hashtagRepository.findByContent(createHashtagRequest.getContent());
        if (hashtag == null) {
            hashtagRepository.save(createHashtagRequest.toEntity());
        } else {
            List<Long> postIdList = hashtag.getPostIdList();
            postIdList.add(createHashtagRequest.getPostId());
            hashtagRepository.update(hashtag, postIdList);
        }
    }

    public void updateHashtag(UpdateHashtagRequestDto updateHashtagRequest) {
        Hashtag hashtag = hashtagRepository.getByContent(updateHashtagRequest.getContent())
                .orElseThrow(() -> new NotFoundException(ResponseType.SEARCH_NOT_EXIST_HASHTAG));
        List<Long> postIdList = hashtag.getPostIdList();
        if (updateHashtagRequest.isStatus()) {
            postIdList.remove(updateHashtagRequest.getPostId());
            postIdList.add(updateHashtagRequest.getPostId());
        } else {
            postIdList.remove(updateHashtagRequest.getPostId());
        }
        hashtagRepository.update(hashtag, postIdList);
    }

    public void deleteHashtag(String content) {
        Hashtag hashtag = hashtagRepository.getByContent(content)
                .orElseThrow(() -> new NotFoundException(ResponseType.SEARCH_NOT_EXIST_HASHTAG));
        hashtagRepository.delete(hashtag);
    }

    public List<HashtagResponseDto> findHashtagsByContent(String content) {
        List<Hashtag> hashtags = hashtagRepository.findByContentContains(content);
        return hashtags.stream()
                .map(HashtagResponseDto::of)
                .collect(Collectors.toList());
    }

}
