package com.xjudge.entity.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserContestKey implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "contest_id")
    private Long contestId;
}
