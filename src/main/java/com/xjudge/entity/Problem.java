package com.xjudge.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "problem")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class)
public class Problem extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String problemCode;

    private String problemTitle;

    private String inputMethod;

    private String outputMethod;

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

    @JsonIgnore
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
