package com.xjudge.entity.problem;

import com.xjudge.entity.BaseEntity;
import com.xjudge.model.enums.SectionFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Entity representing a section of a problem (e.g. statement, input, output, note).*/

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "section")
public class Section extends BaseEntity<Long> {
    // etc.)
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
