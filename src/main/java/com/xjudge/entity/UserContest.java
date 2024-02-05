package com.xjudge.entity;

import com.xjudge.entity.key.UserContestKey;
import com.xjudge.model.enums.UserContestRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_contest")
public class UserContest {

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

    @Enumerated(EnumType.STRING)
    private UserContestRole role;
}
