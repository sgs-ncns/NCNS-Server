package dev.ncns.sns.search.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GlobalResponseDto {

    private final UserResponseDto user;
    private final HashtagResponseDto hashtag;

    @Builder
    private GlobalResponseDto(UserResponseDto user, HashtagResponseDto hashtag) {
        this.user = user;
        this.hashtag = hashtag;
    }

    public static GlobalResponseDto of(UserResponseDto userResponse) {
        return GlobalResponseDto.builder()
                .user(userResponse)
                .hashtag(null)
                .build();
    }

    public static GlobalResponseDto of(HashtagResponseDto hashtagResponse) {
        return GlobalResponseDto.builder()
                .user(null)
                .hashtag(hashtagResponse)
                .build();
    }

}
