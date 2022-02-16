package dev.ncns.sns.user.service;

import dev.ncns.sns.common.domain.ResponseType;
import dev.ncns.sns.common.exception.BadRequestException;
import dev.ncns.sns.user.domain.CountType;
import dev.ncns.sns.user.domain.Follow;
import dev.ncns.sns.user.domain.FollowStatus;
import dev.ncns.sns.user.dto.response.StatusResponseDto;
import dev.ncns.sns.user.dto.response.UserSummaryResponseDto;
import dev.ncns.sns.user.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FollowService {

    private final FollowRepository followRepository;

    private final UserService userService;
    private final UserCountService userCountService;

    @Transactional(readOnly = true)
    public List<UserSummaryResponseDto> getFollowingList(Long userId) {
        userService.checkExistUser(userId);
        return userService.getUserSummaryList(getFollowingIdList(userId));
    }

    @Transactional(readOnly = true)
    public List<UserSummaryResponseDto> getFollowerList(Long userId) {
        userService.checkExistUser(userId);
        return userService.getUserSummaryList(getFollowerIdList(userId));
    }

    /**
     * 팔로우 요청 시 잘못된 요청(자기 자신을 팔로우 하는 요청 등)인지 우선적으로 검토합니다.
     * 요청이 정상이라면 팔로우 상태를 확인 후 토글합니다.
     * 팔로우 정보를 추가/삭제하고, 나의 팔로잉 수와 상대의 팔로워 수를 업데이트합니다.
     */
    @Transactional
    public StatusResponseDto requestFollow(Long userId, Long targetId) {
        checkSameUser(userId, targetId);
        userService.checkExistUser(targetId);
        Follow followData = followRepository.findByUserIdAndTargetId(userId, targetId);
        return followData == null ? follow(userId, targetId) : unfollow(followData);
    }

    @Transactional
    public void deleteFollow(Long userId) {
        List<Long> followingList = getFollowingIdList(userId);
        userCountService.decreaseFollowerCount(followingList);
        followRepository.deleteAllByUserId(userId);

        List<Long> followerList = getFollowerIdList(userId);
        userCountService.decreaseFollowingCount(followerList);
        followRepository.deleteAllByTargetId(userId);
    }

    private List<Long> getFollowingIdList(Long userId) {
        return followRepository.findTargetIdByUserId(userId);
    }

    private List<Long> getFollowerIdList(Long userId) {
        return followRepository.findUserIdByTargetId(userId);
    }

    private void checkSameUser(Long userId, Long targetId) {
        if (userId.equals(targetId)) {
            throw new BadRequestException(ResponseType.REQUEST_NOT_VALID);
        }
    }

    private StatusResponseDto follow(Long userId, Long targetId) {
        Follow follow = Follow.builder().userId(userId).targetId(targetId).build();
        followRepository.save(follow);
        updateFollowCount(userId, targetId, true);
        return StatusResponseDto.of(FollowStatus.FOLLOW.getValue());
    }

    private StatusResponseDto unfollow(Follow followData) {
        updateFollowCount(followData.getUserId(), followData.getTargetId(), false);
        followRepository.delete(followData);
        return StatusResponseDto.of(FollowStatus.UNFOLLOW.getValue());
    }

    private void updateFollowCount(Long userId, Long targetId, boolean isUp) {
        userCountService.updateUserCount(userId, CountType.FOLLOWING, isUp);
        userCountService.updateUserCount(targetId, CountType.FOLLOWER, isUp);
    }
}