package com.xjudge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * <strong>Problem Entity</strong>
 * <p>Problem entity is used to store all problem details</p>
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "problem")
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long problemId;

    private String problemCode;

    private int problemRate;

    private String problemTitle;

    private String inputFile;

    private String outputFile;

    @Column(columnDefinition = "LONGTEXT")
    @Lob
    private String problemStatement;

    @Column(columnDefinition = "LONGTEXT")
    @Lob
    private String problemInput;

    @Column(columnDefinition = "LONGTEXT")
    @Lob
    private String problemOutput;

    private String problemSource;

    @Column(columnDefinition = "LONGTEXT")
    @Lob
    private String problemNote;

    private String problemTimeLimit;

    private String problemMemoryLimit;

    private String problemTutorial;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "problem_id")
    List<Sample> problemSamples;

    @ManyToMany
    @JoinTable(
            name = "problem_tags",
            joinColumns = @JoinColumn(name = "problem_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "problem_id")
    List<Submission> problemSubmission;
}