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
    /**
     * Hashtag 테이블은 태그의 내용과 태그 수로 이루어져있습니다.
     * 검색창에 입력 시 해시태그의 수를 보여줘야 하기 때문에 count 컬럼을 두었습니다.
     * 글에 포함된 해시태그는 , 로 concat 되어 Post 테이블에 저장됩니다.
     * HashTag 객체는 Post와 OneToMany 관계이며, DB
     */
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