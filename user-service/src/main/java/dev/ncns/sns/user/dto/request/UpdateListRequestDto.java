package dev.ncns.sns.user.dto.request;

import dev.ncns.sns.user.domain.ListType;
import lombok.Builder;
import lombok.Getter;


@Getter
public class UpdateListRequestDto {

    private final Long userId;
    private final Long targetId;
    private final Boolean isAdd;
    private final ListType listType;

    @Builder
    public UpdateListRequestDto(Long userId, Long targetId, Boolean isAdd, ListType listType) {
        this.userId = userId;
        this.targetId = targetId;
        this.isAdd = isAdd;
        this.listType = listType;
    }

    public static UpdateListRequestDto of(Long userId, Long targetId, Boolean isAdd, ListType listType) {
        return UpdateListRequestDto.builder()
                .userId(userId)
                .targetId(targetId)
                .isAdd(isAdd)
                .listType(listType)
                .build();
    }
}