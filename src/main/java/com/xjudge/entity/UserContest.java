package com.xjudge.entity;

import com.xjudge.entity.ids.UserContestId;
import com.xjudge.enums.UserContestRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <strong>UserContest Entity</strong>
 * <p>Represents a user's data in a contest. {Favorite}</p>
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_contest")
@IdClass(UserContestId.class)
public class UserContest {

    @Id
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "contest_id")
    private Contest contest;

    @Id
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;

    private Boolean isFavorite;

    @Enumerated(EnumType.STRING)
    private UserContestRole role;
}
