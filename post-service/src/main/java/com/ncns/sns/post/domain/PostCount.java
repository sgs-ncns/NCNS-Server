package com.ncns.sns.post.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

import static com.ncns.sns.post.domain.CountType.COMMENT;
import static com.ncns.sns.post.domain.CountType.LIKE;

@Getter
@ToString
@NoArgsConstructor
@Table(name = "post_count")
@Entity
public class PostCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long likeCount;

    @Column(nullable = false)
    private Long commentCount;

    @Builder
    public PostCount(Long postId) {
        this.postId = postId;
        this.likeCount = 0L;
        this.commentCount = 0L;
    }

    public void update(CountType countType, boolean isUp) {
        if (countType == LIKE) {
            if (isUp) {
                this.likeCount++;
            } else {
                this.likeCount--;
            }
        } else if (countType == COMMENT) {
            if (isUp) {
                this.commentCount++;
            } else {
                this.commentCount--;
            }
        }
    }
}