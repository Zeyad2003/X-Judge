package com.xjudge.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xjudge.entity.key.UserProblemKey;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_problem")
public class UserProblem extends BaseEntity<UserProblemKey> {
    @EmbeddedId
    UserProblemKey id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "problem_id")
    @MapsId("problemId")
    @ToString.Exclude
    @JsonIgnore
    private Problem problem;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    @MapsId("userId")
    @ToString.Exclude
    @JsonIgnore
    private User user;

    private Boolean isFavorite;

    private Boolean isSolved;

    private Boolean isAttempted;

}
