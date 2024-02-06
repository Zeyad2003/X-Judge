package com.xjudge.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sample")
public class Sample extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sampleInput;

    private String sampleOutput;

}

/*
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sample")
public class Sample {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sampleId;

    private String sampleInput;

    private String sampleOutput;
}
 */