//package com.xjudge.entity;
//
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.Date;
//import java.util.List;
//
//@NoArgsConstructor
//@Data
//@Entity
//@AllArgsConstructor
//@Table(name="contests")
//public class Contest {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private int id;
//    @Column(name = "name")
//    private String name;
//    @Column(name = "begin_time")
//    private Date beginTime;
//    @Column(name = "length")
//    private int length;
//    @ManyToMany
//    @JoinTable(
//            name = "problem_contest",
//            joinColumns = @JoinColumn(name = "contest_id"),
//            inverseJoinColumns = @JoinColumn(name = "problem_id")
//    )
//    private List<Problem> problems;
//    @ManyToOne
//    @JoinColumn(name = "group_id")
//    private Group group;
//}
