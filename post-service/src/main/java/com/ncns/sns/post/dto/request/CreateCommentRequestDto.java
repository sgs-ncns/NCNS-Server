package com.ncns.sns.post.dto.request;

import com.ncns.sns.post.common.SecurityUtil;
import com.ncns.sns.post.domain.Comment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
public class CreateCommentRequestDto {
    @NotNull
    private final Long postId;
    @NotBlank
    private final String content;

    private Long parentId;

    public Comment toEntity() {
        return Comment.builder()
                .postId(postId)
                .userId(SecurityUtil.getCurrentMemberId())
                .parentId(parentId)
                .content(content)
                .build();
    }
}