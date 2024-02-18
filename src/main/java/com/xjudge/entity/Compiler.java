package com.xjudge.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "compiler")
public class Compiler extends BaseEntity<Long> {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String idValue;

    private String name;

    @ManyToOne
    @JoinColumn(name = "problem_id", nullable = false)
    @ToString.Exclude
    private Problem problem;

}
