package dev.ncns.sns.feed.dto.request;

import dev.ncns.sns.feed.domain.ListType;
import lombok.Getter;

@Getter
public class UpdateListRequestDto {

    private Long userId;
    private Long targetId;
    private Boolean isAdd;
    private ListType listType;

}
