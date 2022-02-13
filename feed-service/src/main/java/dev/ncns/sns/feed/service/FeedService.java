package dev.ncns.sns.feed.service;

import dev.ncns.sns.feed.domain.FeedDocument;
import dev.ncns.sns.feed.domain.FeedRepository;
import dev.ncns.sns.feed.dto.request.CreateFeedDocumentRequestDto;
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

}