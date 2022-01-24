package dev.ncns.sns.user.service;

import dev.ncns.sns.user.domain.Follow;
import dev.ncns.sns.user.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
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
    public Long isFollowing(Long userId, Long targetId) {
        return followRepository.isExist(userId, targetId).orElse(null);
    }

    @Transactional
    public void unFollow(Long followId) {
        followRepository.deleteById(followId);
    }

    @Transactional
    public void requestFollow(Long userId, Long targetId) {
        if (userId == targetId) {
            throw new IllegalArgumentException("unvalid request");
        }
        Follow follow = Follow.builder().userId(userId).targetId(targetId).build();
        followRepository.save(follow);
    }
}