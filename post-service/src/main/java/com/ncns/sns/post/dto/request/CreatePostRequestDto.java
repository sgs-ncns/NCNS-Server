package com.ncns.sns.post.dto.request;

import com.ncns.sns.post.domain.Post;
import com.ncns.sns.post.util.SecurityUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@NoArgsConstructor
@Getter
public class CreatePostRequestDto {

    @NotBlank(message = "계정명은 공백일 수 없습니다.")
    private String accountName;

    @NotBlank(message = "이미지는 공백일 수 없습니다.")
    private String imagePath;

    private String content;

    private List<String> hashtag;

    private List<Long> usertag;

    public Post toEntity(String hashtag) {
        return Post.builder()
                .userId(SecurityUtil.getCurrentUserId())
                .accountName(accountName)
                .imagePath(imagePath)
                .content(content)
                .hashtag(hashtag)
                .build();
    }
}