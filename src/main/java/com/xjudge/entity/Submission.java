package com.xjudge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor
@Data
@Entity
@AllArgsConstructor
@Table(name="submissions")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "code", columnDefinition = "TEXT")
    private String code;
    @Column(name = "language")
    private String language; // Enum
    @Column(name = "date")
    private Instant date;  //current time
    @Column(name = "result")
    private String result;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "problem_id")
    private Problem problem;
}
