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
@SQLDelete(sql = "update posts set deleted_at=now() where id=?")
@Where(clause = "deleted_at is null")
@Table(name = "posts")
@Entity
public class Post extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String image;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    private String hashtag;

    @Builder
    public Post(Long userId, String content, String image, String hashtag) {
        this.userId = userId;
        this.content = content;
        this.image = image;
        this.hashtag = hashtag;
    }

    public Post updatePost(String content, String hashtag) {
        this.content = content;
        this.hashtag = hashtag;
        return this;
    }
}