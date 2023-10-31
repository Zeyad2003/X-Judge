package com.xjudge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@Entity
@AllArgsConstructor
@Table(name = "problems")
public class Problem {
    // @one to many prolem=> samples ✅
    // num of user solved it
    // contest id ✅
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "title")
    private String title;
    @Column(name = "problem_statement")
    private String problemStatement;
    @Column(name = "constrains")
    private String constrains;
    @Column(name = "input")
    private String input;
    @Column(name = "output")
    private String output;
    @Column(name = "link")
    private String link;
    @Column(name = "note")
    private String note;
    @OneToMany
    private List<Samples> samples;
    @OneToMany
    private List<Tag> tags;
    @OneToOne
    @JoinColumn(name = "tutorial_id")
    private Tutorial tutorial;
    @ManyToOne
    @JoinColumn(name = "rate_id")
    private Rating rate;


}
