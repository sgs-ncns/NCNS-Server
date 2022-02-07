package dev.ncns.sns.user.service;

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

    @Transactional(readOnly = true)
    private Long isFollowing(Long userId, Long targetId) {
        return followRepository.isExist(userId, targetId);
    }

    @Transactional
    public void unFollow(Long followId) {
        followRepository.deleteById(followId);
        //TODO:: current user following count --
        //TODO:: target user follower count --
    }

    @Transactional
    public String requestFollow(Long userId, Long targetId) {
        isSameUser(userId, targetId);
        Long followData = isFollowing(userId, targetId);
        if (followData != null) {
            unFollow(followData);
            return "unfollow";
        }
        Follow follow = Follow.builder().userId(userId).targetId(targetId).build();
        followRepository.save(follow);
        //TODO:: current user following count ++
        //TODO:: target user follower count ++
        return "follow";
    }

    private void isSameUser(Long userId, Long targetId) {
        if (userId == targetId) {
            throw new IllegalArgumentException("unvalid request");
        }
    }
}