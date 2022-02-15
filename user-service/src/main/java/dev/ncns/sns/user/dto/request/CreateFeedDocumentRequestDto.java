package dev.ncns.sns.user.dto.request;

import lombok.Getter;

@Getter
public class CreateFeedDocumentRequestDto {

    private final Long userId;

    private CreateFeedDocumentRequestDto(Long userId) {
        this.userId = userId;
    }

    public static CreateFeedDocumentRequestDto of(Long userId) {
        return new CreateFeedDocumentRequestDto(userId);
    }
}
