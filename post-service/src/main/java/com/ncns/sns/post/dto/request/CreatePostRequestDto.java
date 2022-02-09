package com.ncns.sns.post.dto.request;

import com.ncns.sns.post.common.SecurityUtil;
import com.ncns.sns.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class CreatePostRequestDto {

    @NotBlank(message = "이미지는 공백일 수 없습니다.")
    private final String image;

    private String content;

    private List<String> hashtag;

    private List<Long> usertag;

    public Post toEntity(String hashtag) {
        return Post.builder()
                .userId(SecurityUtil.getCurrentMemberId())
                .image(image)
                .content(content)
                .hashtag(hashtag)
                .build();
    }
}