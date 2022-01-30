package dev.ncns.sns.user.dto;

import dev.ncns.sns.user.domain.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@NoArgsConstructor
public class UserSummaryResponseDto {
    private Long id;
    private String accountName;
    private String nickname;

    public UserSummaryResponseDto(Users user) {
        BeanUtils.copyProperties(user, this);
    }
}