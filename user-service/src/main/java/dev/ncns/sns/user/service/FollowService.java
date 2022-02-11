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

    /**
     * 팔로우 요청 시 잘못된 요청(자기 자신을 팔로우 하는 요청 등)인지 우선적으로 검토합니다.
     * 요청이 정상이라면 팔로우 상태를 확인 후 토글합니다.
     * 팔로우 정보를 추가/삭제하고, 나의 팔로잉 수와 상대의 팔로워 수를 업데이트합니다.
     */
    @Transactional
    public String requestFollow(Long userId, Long targetId) {
        isSameUser(userId, targetId);
        Follow followData = followRepository.findByUserIdAndTargetId(userId, targetId);
        return followData == null ? follow(userId, targetId) : unFollow(followData);
    }

    private void isSameUser(Long userId, Long targetId) {
        if (userId.equals(targetId)) {
            throw new BadRequestException(ResponseType.REQUEST_NOT_VALID);
        }
    }

    private String follow(Long userId, Long targetId) {
        Follow follow = Follow.builder().userId(userId).targetId(targetId).build();
        followRepository.save(follow);
        updateFollowCount(userId, targetId, true);
        return "follow";
    }

    private String unFollow(Follow followData) {
        updateFollowCount(followData.getUserId(), followData.getTargetId(), false);
        followRepository.delete(followData);
        return "unfollow";
    }


    private void updateFollowCount(Long userId, Long targetId, boolean isUp) {
        UserCount userCount = userCountRepository.findByUserId(userId);
        UserCount targetCount = userCountRepository.findByUserId(targetId);

        /** 팔로우/팔로잉은 마이너스가 될 수 없습니다. */
        if (isUp == false && (userCount.getFollowingCount() <= 0 || targetCount.getFollowerCount() <= 0)) {
            throw new BadRequestException(ResponseType.REQUEST_NOT_VALID);
        }
        userCount.update(CountType.FOLLOWING, isUp);
        targetCount.update(CountType.FOLLOWER, isUp);
    }
}