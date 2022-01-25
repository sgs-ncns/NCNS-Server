package dev.ncns.sns.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class ProfileUpdateRequestDto {
    private String account;
    private String nickname;
    private String introduce;
}