package dev.ncns.sns.post.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
        if (countType == CountType.LIKE) {
            if (isUp) {
                this.likeCount++;
            } else {
                this.likeCount--;
            }
        } else if (countType == CountType.COMMENT) {
            if (isUp) {
                this.commentCount++;
            } else {
                this.commentCount--;
            }
        }
    }

}
