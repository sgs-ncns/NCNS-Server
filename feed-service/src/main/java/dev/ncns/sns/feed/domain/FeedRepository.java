package dev.ncns.sns.feed.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FeedRepository extends MongoRepository<FeedDocument, String> {

    Optional<FeedDocument> findByUserId(Long userId);
}
