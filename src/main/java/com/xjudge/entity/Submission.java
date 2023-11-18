package com.xjudge.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <strong>Submission Entity</strong>
 * <p>Submission entity is used to store all submission details</p>
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "submission")
public class Submission {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long submissionId;

  private String submissionUserHandle;

  private String submissionProblemId;

  @Column(columnDefinition = "TEXT") private String submissionCode;

  private String submissionLanguage;

  private Instant submissionTime;

  private String submissionVerdict;

  private BigDecimal submissionMemoryUsage;

  private BigDecimal submissionTimeUsage;
}
