package dev.ncns.sns.feed.dto.response;

import dev.ncns.sns.feed.domain.Feed;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class FeedResponseDto {
    private boolean result;
    private boolean endOfFeed;
    private List<Feed> feeds;

    @Builder
    public FeedResponseDto(List<Feed> feeds) {
        this.result = true;
        this.endOfFeed = false;
        this.feeds = feeds;
    }

    public static FeedResponseDto of(List<Feed> feeds) {
        return FeedResponseDto.builder()
                .feeds(feeds)
                .build();
    }

    public void updateFailPullResult() {
        this.result = false;
    }

    public void setEndOfFeed() {
        this.endOfFeed = true;
    }

}