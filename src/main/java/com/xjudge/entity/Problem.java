package com.xjudge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "problem")
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_id")
    private Long problemId;

    @Column(name = "problem_title")
    private String problemTitle;

    @Column(name = "problem_statement")
    private String problemStatement;

    @Column(name = "problem_input")
    private String problemInput;

    @Column(name = "problem_output")
    private String problemOutput;

    @Column(name = "problem_sample_input")
    private String problemSampleInput;

    @Column(name = "problem_sample_output")
    private String problemSampleOutput;

    @Column(name = "problem_source")
    private String problemSource;

    @Column(name = "problem_note")
    private String problemNote;

    @Column(name = "problem_time_limit")
    private String problemTimeLimit;

    @Column(name = "problem_memory_limit")
    private String problemMemoryLimit;

    @Column(name = "problem_tutorial")
    private String problemTutorial;

    @ManyToMany
    @JoinTable(
            name = "problem_tags",
            joinColumns = @JoinColumn(name = "problem_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;
}
