package dev.ncns.sns.search.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateUserRequestDto {

    private final Long userId;
    private final String accountName;
    private final String nickname;

}
