package com.xjudge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <strong>UserStatistics entity.</strong>
 * <p style="color: aqua;">In the future, we may modify or add more fields related to statistics like (attempted, codeforces solved, etc.)</p>
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "statistics")
public class Statistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statistics_id")
    private Long statisticsId;

    @Column(name = "solved_count")
    Long solvedCount;
}
