package dev.ncns.sns.feed.dto.request;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
public class FeedPullRequestDto {

    private List<Long> followingList;
    private LocalDateTime lastUpdated;

}