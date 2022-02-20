package dev.ncns.sns.feed.dto.response;

import dev.ncns.sns.feed.domain.Feed;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class SubscribingFeedResponseDto {
    private Long userId;
    private List<Feed> recentFeeds = new ArrayList<>();

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void add(Feed feed) {
        this.recentFeeds.add(feed);
    }
}