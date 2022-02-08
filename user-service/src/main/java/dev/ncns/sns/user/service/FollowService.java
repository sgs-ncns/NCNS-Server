package dev.ncns.sns.user.service;

import dev.ncns.sns.common.domain.ResponseType;
import dev.ncns.sns.common.exception.BadRequestException;
import dev.ncns.sns.user.domain.Follow;
import dev.ncns.sns.user.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FollowService {

    private final FollowRepository followRepository;

    @Transactional(readOnly = true)
    public List<Long> getFollowingIdList(Long userId) {
        return followRepository.findTargetIdByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Long> getFollowerIdList(Long userId) {
        return followRepository.findUserIdByTargetId(userId);
    }

    @Transactional
    public String requestFollow(Long userId, Long targetId) {
        isSameUser(userId, targetId);
        Long followData = followRepository.findFollowId(userId, targetId);
        return followData == null ? follow(userId, targetId) : unFollow(followData);
    }

    private String follow(Long userId, Long targetId) {
        Follow follow = Follow.builder().userId(userId).targetId(targetId).build();
        followRepository.save(follow);
        //TODO:: current user following count ++
        //TODO:: target user follower count ++
        return "follow";
    }

    private String unFollow(Long followId) {
        followRepository.deleteById(followId);
        //TODO:: current user following count --
        //TODO:: target user follower count --
        return "unfollow";
    }

    private void isSameUser(Long userId, Long targetId) {
        if (userId.equals(targetId)) {
            throw new BadRequestException(ResponseType.REQUEST_NOT_VALID);
        }
    }

}
