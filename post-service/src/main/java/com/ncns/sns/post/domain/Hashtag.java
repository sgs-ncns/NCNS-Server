package com.ncns.sns.post.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor
@Table(name = "hashtag")
@Entity
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String content;

    @Column(nullable = false)
    private int count;

    @Builder
    public Hashtag(String content, int count) {
        this.content = content;
        this.count = count;
    }
}