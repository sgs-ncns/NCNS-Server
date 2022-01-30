package dev.ncns.sns.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class ProfileUpdateRequestDto {
    private String accountName;
    private String nickname;
    private String introduce;
}