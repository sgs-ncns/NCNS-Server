package com.ncns.sns.post.dto.request;

import com.ncns.sns.post.util.SecurityUtil;
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

    @NotNull
    private final String accountName;

    @NotBlank
    private final String content;

    private Long parentId;

    public Comment toEntity() {
        return Comment.builder()
                .postId(postId)
                .userId(SecurityUtil.getCurrentUserId())
                .accountName(accountName)
                .parentId(parentId)
                .content(content)
                .build();
    }
}