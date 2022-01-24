package dev.ncns.sns.user.domain;

import lombok.*;
import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Table(name = "follow")
public class Follow extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long targetId;

    @Builder
    public Follow(Long userId, Long targetId) {
        this.userId = userId;
        this.targetId = targetId;
    }
}
