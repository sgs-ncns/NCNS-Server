package com.ncns.sns.post.dto.request;

import com.ncns.sns.post.common.SecurityUtil;
import com.ncns.sns.post.domain.Post;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CreatePostRequestDto {

    @NotBlank(message = "이미지는 공백일 수 없습니다.")
    private String image;

    private String content;

    private String hashtag;

    public Post toEntity() {
        return Post.builder()
                .userId(SecurityUtil.getCurrentMemberId())
                .image(image)
                .content(content)
                .hashtag(hashtag)
                .build();
    }
}