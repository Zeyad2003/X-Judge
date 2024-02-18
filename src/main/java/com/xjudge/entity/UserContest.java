package com.xjudge.entity;

import com.xjudge.entity.key.UserContestKey;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_contest")
public class UserContest extends BaseEntity<UserContestKey> {

    @EmbeddedId
    UserContestKey id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "contest_id")
    @MapsId("contestId")
    private Contest contest;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    @MapsId("userId")
    private User user;

    private Boolean isFavorite;

    private Boolean isOwner = false;

    private Boolean isParticipant;

    private Integer userContestPenalty;

    private Integer userContestRank;

    private Integer userContestScore;

}
