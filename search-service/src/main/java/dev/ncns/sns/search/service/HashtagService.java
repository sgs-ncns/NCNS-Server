package dev.ncns.sns.search.service;

import dev.ncns.sns.common.domain.ResponseType;
import dev.ncns.sns.common.exception.NotFoundException;
import dev.ncns.sns.search.domain.Hashtag;
import dev.ncns.sns.search.dto.request.CreateHashtagRequestDto;
import dev.ncns.sns.search.dto.response.HashtagResponseDto;
import dev.ncns.sns.search.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
            // TODO: Update
        }
    }

}
