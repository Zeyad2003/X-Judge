package com.xjudge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <strong>UserStatistics entity.</strong>
 * <p style="color: aqua;">In the future, we may modify or add more fields or tables related to statistics like (CodeForces or AtCoder solved problems count, etc.)</p>
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_statistics")
public class UserStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userStatisticsId;

    Long userSolvedCount;

    Long userAttemptedCount;
}
