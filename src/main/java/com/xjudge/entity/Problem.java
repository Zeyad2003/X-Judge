package com.xjudge.entity;

import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.util.jpa.MapToJsonConverter;
import jakarta.persistence.*;
import lombok.*;
import java.util.Map;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "problems", uniqueConstraints = @UniqueConstraint(columnNames = { "code", "online_judge" }))
public class Problem extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code; // e.g., 2134C

    @Enumerated(EnumType.STRING)
    @Column(name = "online_judge", nullable = false)
    private OnlineJudgeType onlineJudge;

    @Column(nullable = false)
    private String title;

    private String contestName; // may be null for some OJs

    @Column(name = "problem_url")
    private String problemUrl;

    @Column(name = "contest_url")
    private String contestUrl;

    @Lob
    @Column(name = "constraints")
    private String constraints; // time/memory/input/output summary as a blob

    @Lob
    @Convert(converter = MapToJsonConverter.class)
    @Column(name = "sections")
    private Map<String, Object> sections;

    @Lob
    @Convert(converter = MapToJsonConverter.class)
    @Column(name = "samples")
    private Map<String, Object> samples;

    @Override
    public Long getId() {
        return id;
    }
}
