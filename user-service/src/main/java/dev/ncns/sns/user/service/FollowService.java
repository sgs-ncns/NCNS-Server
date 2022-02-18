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

    public boolean getFollowStatus(Long userId, Long targetId) {
        return followRepository.existsByUserIdAndTargetId(userId, targetId);
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
