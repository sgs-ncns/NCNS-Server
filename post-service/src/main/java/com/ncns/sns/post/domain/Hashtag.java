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

    @Column(nullable = false, length = 50, unique = true)
    private String content;

    @Column(nullable = false)
    private int count;

    @Builder
    public Hashtag(String content, int count) {
        this.content = content;
        this.count = count;
    }

    public void update(boolean isUp) {
        if (isUp) {
            this.count++;
        } else {
            this.count--;
        }
    }
}