package dev.ncns.sns.post.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
