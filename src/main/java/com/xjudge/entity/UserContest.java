package com.xjudge.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
public class UserContest {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "contest_id")
    private Long contestId;

    private Boolean isFavorite;
}
