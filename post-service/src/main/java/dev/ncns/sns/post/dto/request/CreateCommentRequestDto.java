package dev.ncns.sns.post.dto.request;

import dev.ncns.sns.post.domain.Comment;
import dev.ncns.sns.post.util.SecurityUtil;
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
