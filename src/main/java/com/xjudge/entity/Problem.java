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
@Table(name = "problem")
public class Problem {
    // @one to many prolem=> samples ✅
    // num of user solved it
    //contest id ✅
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String problemStatement;
    private String constraints;
    private String input;
    private String output;
    private String link;
    private String note;
    @OneToMany
    private List<Samples> samples;
    @OneToMany
    private List<Tag> tags;
    @OneToOne
    private Tutorial tutorial;
    @ManyToOne
    private Rating rate;


}
