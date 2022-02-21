package dev.ncns.sns.feed.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "feed")
public class FeedDocument {

    @Id
    @Field(targetType = FieldType.OBJECT_ID)
    private String id;

    @Field
    @Indexed(unique = true)
    private Long userId;

    @Field("updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Field("followings")
    private List<Long> followings;

    @Field("followers")
    private List<Long> followers;

    @Field("subscribings")
    private List<Long> subscribings;

    @Field("subscribers")
    private List<Long> subscribers;

    @Field("feeds")
    private List<Feed> feeds;

    @Field("recentSubscribing")
    private List<Long> recentSubscribing;

    @Builder
    public FeedDocument(Long userId) {
        this.userId = userId;
        this.updatedAt = LocalDateTime.now();
        this.followings = new ArrayList<>();
        this.followers = new ArrayList<>();
        this.subscribings = new ArrayList<>();
        this.subscribers = new ArrayList<>();
        this.feeds = new ArrayList<>();
        this.recentSubscribing = new ArrayList<>();
    }

    public void updateFollowingFeed(List<Feed> newFeeds) {
        this.feeds.addAll(newFeeds);
        this.updatedAt = LocalDateTime.now();
    }

    public void updateSubscribeFeed(Feed newSubscribeFeed) {
        this.feeds.add(newSubscribeFeed);
        Long subscribing = newSubscribeFeed.getUserId();
        this.recentSubscribing.remove(subscribing);
        this.recentSubscribing.add(newSubscribeFeed.getUserId());
    }

    public void addToList(Long targetId, ListType listType) {
        switch (listType) {
            case FOLLOWING:
                this.followings.add(targetId);
                break;
            case FOLLOWER:
                this.followers.add(targetId);
                break;
            case SUBSCRIBING:
                this.subscribings.add(targetId);
                break;
            case SUBSCRIBER:
                this.subscribers.add(targetId);
                break;
        }
    }

    public void removeFromList(Long targetId, ListType listType) {
        switch (listType) {
            case FOLLOWING:
                this.followings.remove(targetId);
                break;
            case FOLLOWER:
                this.followers.remove(targetId);
                break;
            case SUBSCRIBING:
                this.subscribings.remove(targetId);
                break;
            case SUBSCRIBER:
                this.subscribers.remove(targetId);
                break;
        }
    }

}
