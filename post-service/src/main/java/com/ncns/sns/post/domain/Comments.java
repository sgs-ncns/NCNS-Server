package com.ncns.sns.post.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor
@SQLDelete(sql = "update comments set deleted_at=now() where id=?")
@Where(clause = "deleted_at is null")
@Table(name = "comments")
@Entity
public class Comments extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long userId;

    @Column
    private Long parentId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Builder
    public Comments(Long postId, Long userId, Long parentId, String content) {
        this.postId = postId;
        this.userId = userId;
        this.parentId = parentId;
        this.content = content;
    }
}
