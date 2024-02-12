package com.xjudge.entity;

import com.xjudge.entity.converter.JsonDataConverter;
import com.xjudge.model.enums.OnlineJudgeType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

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

    private String userHandle;

    private String problemCode;

    private String remoteRunId;

    private OnlineJudgeType ojType;

    @Column(columnDefinition = "LONGTEXT")
    @Size(min = 20, max = 65535)
    private String solution;

    private String language;

    private Instant submitTime;

    private String memoryUsage;

    private String timeUsage;

    private String verdict;

    private String submissionStatus; // kept updated (submitted, in queue, running test 14, Accepted)

    @Convert(converter = JsonDataConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> extraInfo = new HashMap<>(); // Any extra info like contest id, etc.

}
