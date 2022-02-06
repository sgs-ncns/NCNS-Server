package com.ncns.sns.post.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor
@Table(name = "posts_count")
@Entity
public class PostCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private int likeCount;

    @Column(nullable = false)
    private int commentCount;

    @Builder
    public PostCount(Long postId, int likeCount, int commentCount) {
        this.postId = postId;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

    //TODO:: like,comment count +- methods
}