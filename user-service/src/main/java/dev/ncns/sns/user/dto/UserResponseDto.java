package dev.ncns.sns.user.dto;

import dev.ncns.sns.user.domain.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;


@Getter
@Setter
@NoArgsConstructor
public class UserResponseDto {

    private Long id;
    private String accountName;
    private String nickname;
    private String introduce;

    public UserResponseDto(Users user) {
        BeanUtils.copyProperties(user, this);
    }
}