package dev.ncns.sns.post.domain;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "update comments set deleted_at=now() where id=?")
@Where(clause = "deleted_at is null")
@Table(name = "comments")
@Entity
public class Comment extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String accountName;

    @Column
    private Long parentId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Builder
    public Comment(Long postId, Long userId, String accountName, Long parentId, String content) {
        this.postId = postId;
        this.userId = userId;
        this.accountName = accountName;
        this.parentId = parentId;
        this.content = content;
    }

    public void updateComment(String content) {
        this.content = content;
    }

}
