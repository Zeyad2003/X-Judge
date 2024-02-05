package com.xjudge.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "problem")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "problemId")
public class Problem extends BaseEntity {

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
    @ToString.Exclude
    List<Sample> problemSamples;

    @ManyToMany
    @JoinTable(
            name = "problem_tags",
            joinColumns = @JoinColumn(name = "problem_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @ToString.Exclude
    private List<Tag> tags;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "problem_id")
    @ToString.Exclude
    List<Submission> problemSubmission;

}
