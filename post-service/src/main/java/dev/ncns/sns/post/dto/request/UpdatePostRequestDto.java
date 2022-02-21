package dev.ncns.sns.post.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class UpdatePostRequestDto {

    private Long postId;
    private String content;
    private List<String> hashtag;
    private List<Long> usertag;

}