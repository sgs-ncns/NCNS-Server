package dev.ncns.sns.user.service;

import dev.ncns.sns.common.domain.ResponseType;
import dev.ncns.sns.common.exception.BadRequestException;
import dev.ncns.sns.user.domain.CountType;
import dev.ncns.sns.user.domain.Subscribe;
import dev.ncns.sns.user.domain.SubscribeStatus;
import dev.ncns.sns.user.dto.response.StatusResponseDto;
import dev.ncns.sns.user.dto.response.UserSummaryResponseDto;
import dev.ncns.sns.user.repository.FollowRepository;
import dev.ncns.sns.user.repository.SubscribeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SubscribeService {

    private final SubscribeRepository subscribeRepository;
    private final FollowRepository followRepository;

    private final UserService userService;
    private final UserCountService userCountService;

    @Transactional(readOnly = true)
    public List<UserSummaryResponseDto> getSubscribingList(Long userId) {
        userService.checkExistUser(userId);
        return userService.getUserSummaryList(getSubscribingIdList(userId));
    }

    @Transactional
    public StatusResponseDto requestSubscribe(Long userId, Long targetId) {
        checkSameUser(userId, targetId);
        userService.checkExistUser(targetId);
        checkFollowTarget(userId, targetId);
        Subscribe subscribe = subscribeRepository.findByUserIdAndTargetId(userId, targetId);
        return subscribe == null ? subscribe(userId, targetId) : unsubscribe(subscribe);
    }

    @Transactional
    public StatusResponseDto unsubscribeByUnfollow(Long userId, Long targetId) {
        Subscribe subscribe = subscribeRepository.findByUserIdAndTargetId(userId, targetId);
        if (subscribe != null) {
            return unsubscribe(subscribe);
        }
        return StatusResponseDto.of(SubscribeStatus.SUBSCRIBE.getValue());
    }

    @Transactional
    public void deleteSubscribe(Long userId) {
        subscribeRepository.deleteAllByUserId(userId);

        List<Long> subscriberList = getSubscriberIdList(userId);
        userCountService.decreaseSubscribingCount(subscriberList);
        subscribeRepository.deleteAllByTargetId(userId);
    }

    public boolean getSubscribeStatus(Long userId, Long targetId) {
        return subscribeRepository.existsByUserIdAndTargetId(userId, targetId);
    }

    private List<Long> getSubscribingIdList(Long userId) {
        return subscribeRepository.findTargetIdByUserId(userId);
    }

    private List<Long> getSubscriberIdList(Long userId) {
        return subscribeRepository.findUserIdByTargetId(userId);
    }

    private void checkSameUser(Long userId, Long targetId) {
        if (userId.equals(targetId)) {
            throw new BadRequestException(ResponseType.REQUEST_NOT_VALID);
        }
    }

    private void checkFollowTarget(Long userId, Long targetId) {
        if (followRepository.findByUserIdAndTargetId(userId, targetId) == null) {
            throw new BadRequestException(ResponseType.REQUEST_NOT_VALID);
        }
    }

    private StatusResponseDto subscribe(Long userId, Long targetId) {
        Subscribe subscribe = Subscribe.builder().userId(userId).targetId(targetId).build();
        subscribeRepository.save(subscribe);
        updateSubscribeCount(userId, true);
        return StatusResponseDto.of(SubscribeStatus.SUBSCRIBE.getValue());
    }

    private StatusResponseDto unsubscribe(Subscribe subscribe) {
        updateSubscribeCount(subscribe.getUserId(), false);
        subscribeRepository.delete(subscribe);
        return StatusResponseDto.of(SubscribeStatus.UNSUBSCRIBE.getValue());
    }

    private void updateSubscribeCount(Long userId, boolean isUp) {
        userCountService.updateUserCount(userId, CountType.SUBSCRIBING, isUp);
    }

}
