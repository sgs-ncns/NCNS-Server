package dev.ncns.sns.post.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class PostSummaryRequestDto {

    private List<Long> postIdList;

}
