package com.xjudge.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.xjudge.util.JsonDataConverter;
import com.xjudge.model.enums.OnlineJudgeType;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "problem")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Problem extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String problemCode;

    private String title;

    @Column(columnDefinition = "LONGTEXT")
    @Lob
    private String statement;

    @Column(columnDefinition = "LONGTEXT")
    @Lob
    private String input;

    @Column(columnDefinition = "LONGTEXT")
    @Lob
    private String output;

    @Enumerated(EnumType.STRING)
    private OnlineJudgeType source;

    private String timeLimit;

    private String memoryLimit;

    @OneToMany(mappedBy = "problem",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    List<Submission> submissions;

    @Convert(converter = JsonDataConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> extraInfo = new HashMap<>();

}
