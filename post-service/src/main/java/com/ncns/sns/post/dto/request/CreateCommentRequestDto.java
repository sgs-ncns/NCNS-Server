package com.ncns.sns.post.dto.request;

import com.ncns.sns.post.common.SecurityUtil;
import com.ncns.sns.post.domain.Comment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@RequiredArgsConstructor
public class CreateCommentRequestDto {

    @NotBlank
    private final Long postId;
    @NotBlank
    private final String conent;

    private Long parentId;

    public Comment toEntity() {
        return Comment.builder()
                .postId(postId)
                .userId(SecurityUtil.getCurrentMemberId())
                .parentId(parentId)
                .content(conent)
                .build();
    }
}