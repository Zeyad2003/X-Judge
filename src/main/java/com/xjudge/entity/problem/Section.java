package com.xjudge.entity.problem;

import com.xjudge.entity.BaseEntity;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.model.enums.SectionFormat;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "section")
public class Section extends BaseEntity<Long> { // split every problem into a multiple section (statement, input, output, note, etc.)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    private String title;

    @Enumerated(EnumType.STRING)
    private SectionFormat sectionFormat;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    private Integer sectionOrder; // To maintain the order of sections

}