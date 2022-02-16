package dev.ncns.sns.feed.dto.response;

import dev.ncns.sns.feed.domain.Feed;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class FeedResponseDto {
    private List<Feed> followingFeed;
    private List<Feed> subscribingFeed;

    @Builder
    public FeedResponseDto(List<Feed> followingFeed, List<Feed> subscribingFeed) {
        this.followingFeed = followingFeed;
        this.subscribingFeed = subscribingFeed;
    }

    public static FeedResponseDto of(List<Feed> followingFeed, List<Feed> subscribingFeed) {
        return FeedResponseDto.builder()
                .followingFeed(followingFeed)
                .subscribingFeed(subscribingFeed)
                .build();
    }
}