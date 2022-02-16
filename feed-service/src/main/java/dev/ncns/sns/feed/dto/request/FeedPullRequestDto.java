package dev.ncns.sns.feed.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class FeedPullRequestDto {

    private List<Long> followingList;
    private LocalDateTime lastUpdated;

}