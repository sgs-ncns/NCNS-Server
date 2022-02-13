package com.ncns.sns.post.dto.request;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class FeedPullRequestDto {

    private List<Long> followingList;
    private LocalDateTime lastUpdated;

}