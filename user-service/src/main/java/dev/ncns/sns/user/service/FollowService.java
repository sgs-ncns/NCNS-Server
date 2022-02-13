package dev.ncns.sns.user.service;

import dev.ncns.sns.common.domain.ResponseType;
import dev.ncns.sns.common.exception.BadRequestException;
import dev.ncns.sns.user.domain.CountType;
import dev.ncns.sns.user.domain.Follow;
import dev.ncns.sns.user.domain.UserCount;
import dev.ncns.sns.user.repository.FollowRepository;
import dev.ncns.sns.user.repository.UserCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserCountRepository userCountRepository;

    @Transactional(readOnly = true)
    public List<Long> getFollowingIdList(Long userId) {
        return followRepository.findTargetIdByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Long> getFollowerIdList(Long userId) {
        return followRepository.findUserIdByTargetId(userId);
    }

    @Transactional
    public Boolean requestFollow(Long userId, Long targetId) {
        isSameUser(userId, targetId);
        Follow followData = followRepository.findByUserIdAndTargetId(userId, targetId);
        return followData == null ? follow(userId, targetId) : unFollow(followData);
    }

    private Boolean follow(Long userId, Long targetId) {
        Follow follow = Follow.builder().userId(userId).targetId(targetId).build();
        followRepository.save(follow);
        updateFollowCount(userId, targetId, true);
        return true;
    }

    private Boolean unFollow(Follow followData) {
        updateFollowCount(followData.getUserId(), followData.getTargetId(), false);
        followRepository.delete(followData);
        return false;
    }

    private void isSameUser(Long userId, Long targetId) {
        if (userId.equals(targetId)) {
            throw new BadRequestException(ResponseType.REQUEST_NOT_VALID);
        }
    }

    private void updateFollowCount(Long userId, Long targetId, boolean isUp) {
        UserCount userCount = userCountRepository.findByUserId(userId);
        UserCount targetCount = userCountRepository.findByUserId(targetId);
        if (isUp == false && (userCount.getFollowingCount() <= 0 || targetCount.getFollowerCount() <= 0)) {
            throw new BadRequestException(ResponseType.REQUEST_NOT_VALID);
        }
        userCount.update(CountType.FOLLOWING, isUp);
        targetCount.update(CountType.FOLLOWER, isUp);
    }
}