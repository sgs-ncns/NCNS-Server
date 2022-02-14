package dev.ncns.sns.feed.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class CreateFeedDocumentRequestDto {
    @NotNull
    private Long userId;
}
