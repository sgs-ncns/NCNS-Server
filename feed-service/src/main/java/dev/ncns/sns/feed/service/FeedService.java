package dev.ncns.sns.feed.service;

import dev.ncns.sns.common.domain.ResponseType;
import dev.ncns.sns.common.exception.BadRequestException;
import dev.ncns.sns.feed.domain.FeedDocument;
import dev.ncns.sns.feed.domain.FeedRepository;
import dev.ncns.sns.feed.dto.request.CreateFeedDocumentRequestDto;
import dev.ncns.sns.feed.dto.request.FollowUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FeedService {

    private final FeedRepository feedRepository;

    @Transactional
    public void createFeedDocument(CreateFeedDocumentRequestDto dto) {
        FeedDocument feedDocument = FeedDocument.builder().userId(dto.getUserId()).build();
        feedRepository.save(feedDocument);
    }


    @Transactional
    public void updateFollowings(FollowUpdateRequestDto dto) {
        FeedDocument feedDocument = getFeedDocument(dto.getUserId());
        feedDocument.updateFollowings(dto.getTargetId(), dto.getIsAdd());
        feedRepository.save(feedDocument);
    }

    private FeedDocument getFeedDocument(Long userId) {
        return feedRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException(ResponseType.USER_NOT_EXIST_ID));
    }
}