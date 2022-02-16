package dev.ncns.sns.feed.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
@NoArgsConstructor
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

    @Field("subscribing")
    private List<Long> subscribing;

    @Field("feeds")
    private List<Feed> feeds;

    @Builder
    public FeedDocument(Long userId) {
        this.userId = userId;
        this.updatedAt = LocalDateTime.now();
        this.followings = new ArrayList<>();
        this.subscribing = new ArrayList<>();
        this.feeds = new ArrayList<>();
    }

    public void updateFeed(List<Feed> newFeeds) {
        this.feeds.addAll(newFeeds);
        this.updatedAt = LocalDateTime.now();
    }

    public void addToList(Long targetId, ListType listType) {
        switch (listType) {
            case FOLLOWING:
                this.followings.add(targetId);
                break;
            case SUBSCRIBING:
                this.subscribing.add(targetId);
                break;
        }
    }

    public void removeFromList(Long targetId, ListType listType) {
        switch (listType) {
            case FOLLOWING:
                this.followings.remove(targetId);
                break;
            case SUBSCRIBING:
                this.subscribing.remove(targetId);
                break;
        }
    }

}