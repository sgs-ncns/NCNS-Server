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

import javax.validation.constraints.NotNull;
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
    @NotNull
    @Indexed(unique=true)
    private Long userId;

    @Field("updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Field("followings")
    @NotNull
    private List<Long> followings;

    @Field("feeds")
    @NotNull
    private List<Feed> feeds;

    @Builder
    public FeedDocument(Long userId) {
        this.userId = userId;
        this.followings = new ArrayList<Long>();
        this.feeds = new ArrayList<Feed>();
        // TODO: 수정
    }

    public void updateFeed(List<Feed> newFeeds) {
        this.feeds.addAll(newFeeds);
        // TODO:: newFeeds.foreach(f -> 기존피드.push(f))
    }

    public void updateFollowings(Long followingId, boolean isAdd) {
        if(isAdd){
            this.followings.add(followingId);
        }
        else {
            this.followings.remove(followingId); // TODO: By index 인지 Object인지
        }
    }
}
