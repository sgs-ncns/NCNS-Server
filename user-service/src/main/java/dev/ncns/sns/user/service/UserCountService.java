package dev.ncns.sns.user.service;

import dev.ncns.sns.common.domain.ResponseType;
import dev.ncns.sns.common.exception.BadRequestException;
import dev.ncns.sns.user.domain.UserCount;
import dev.ncns.sns.user.repository.UserCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserCountService {

    private final UserCountRepository userCountRepository;

    public UserCount getUserCount(Long userId) {
        return userCountRepository.findByUserId(userId);
    }

    public void checkNegativeNumber(long count) {
        if (count <= 0) {
            throw new BadRequestException(ResponseType.REQUEST_NOT_VALID);
        }
    }

}
