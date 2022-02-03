package com.ncns.sns.post.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor
@Table(name = "usertag")
@Entity
public class UserTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long userId;

    @Builder
    public UserTag(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }
}