package dev.ncns.sns.user.service;

import dev.ncns.sns.common.domain.ResponseType;
import dev.ncns.sns.common.exception.BadRequestException;
import dev.ncns.sns.user.domain.CountType;
import dev.ncns.sns.user.domain.UserCount;
import dev.ncns.sns.user.repository.UserCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserCountService {

    private final UserCountRepository userCountRepository;

    public UserCount getUserCount(Long userId) {
        return userCountRepository.findByUserId(userId);
    }

    @Transactional
    public void createUserCount(Long userId) {
        UserCount userCount = UserCount.builder().userId(userId).build();
        userCountRepository.save(userCount);
    }

    @Transactional
    public void updateUserCount(Long userId, CountType countType, boolean isUp) {
        UserCount userCount = getUserCount(userId);
        if (isUp) {
            userCount.increaseCount(countType);
        } else {
            checkNegativeNumber(getCount(userCount, countType));
            userCount.decreaseCount(countType);
        }
    }

    @Transactional
    public void deleteUserCount(Long userId) {
        userCountRepository.deleteByUserId(userId);
    }

    @Transactional
    public void decreaseFollowerCount(List<Long> userIdList) {
        userCountRepository.updateDecreaseFollowerCountByUserId(userIdList);
    }

    @Transactional
    public void decreaseFollowingCount(List<Long> userIdList) {
        userCountRepository.updateDecreaseFollowingCountByUserId(userIdList);
    }

    @Transactional
    public void decreaseSubscribingCount(List<Long> userIdList) {
        userCountRepository.updateDecreaseSubscribingCountByUserId(userIdList);
    }

    private void checkNegativeNumber(Long count) {
        if (count == null || count <= 0) {
            throw new BadRequestException(ResponseType.REQUEST_NOT_VALID);
        }
    }

    private Long getCount(UserCount userCount, CountType countType) {
        switch (countType) {
            case POST:
                return userCount.getPostCount();
            case FOLLOWER:
                return userCount.getFollowerCount();
            case FOLLOWING:
                return userCount.getFollowingCount();
            case SUBSCRIBING:
                return userCount.getSubscribingCount();
        }
        return null;
    }

}
