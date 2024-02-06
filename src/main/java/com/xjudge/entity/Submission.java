package com.xjudge.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="submission")
public class Submission extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String submissionUserHandle;

    private String submissionProblemId;

    @Column(columnDefinition = "TEXT")
    private String submissionCode;

    private String submissionLanguage;

    private Instant submissionTime;

    private String submissionVerdict;

    private BigDecimal submissionMemoryUsage;

    private BigDecimal submissionTimeUsage;

}
