package dev.ncns.sns.search.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SearchResponseDto {

    private final List<GlobalResponseDto> globals;
    private final List<UserResponseDto> users;
    private final List<HashtagResponseDto> hashtags;

    @Builder
    public SearchResponseDto(List<GlobalResponseDto> globals, List<UserResponseDto> users, List<HashtagResponseDto> hashtags) {
        this.globals = globals;
        this.users = users;
        this.hashtags = hashtags;
    }

    public static SearchResponseDto of(List<UserResponseDto> users, List<HashtagResponseDto> hashtags) {
        List<GlobalResponseDto> globals = new ArrayList<>();
        int minSize = Math.min(users.size(), hashtags.size());
        for (int i = 0; i < minSize; i++) {
            globals.add(GlobalResponseDto.of(users.get(i)));
            globals.add(GlobalResponseDto.of(hashtags.get(i)));
        }
        if (minSize == users.size()) {
            for (int i = minSize; i < hashtags.size(); i++) {
                globals.add(GlobalResponseDto.of(hashtags.get(i)));
            }
        } else {
            for (int i = minSize; i < users.size(); i++) {
                globals.add(GlobalResponseDto.of(users.get(i)));
            }
        }
        return SearchResponseDto.builder()
                .globals(globals)
                .users(users)
                .hashtags(hashtags)
                .build();
    }

}
