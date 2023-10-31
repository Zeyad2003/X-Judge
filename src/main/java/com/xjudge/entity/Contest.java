package com.xjudge.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
@AllArgsConstructor
@Table(name="contest")
public class Contest {
    //standing @one to one
    //problems @many to many ✅
    //button status for all submission
    //group id @many to one ✅

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private Date beginTime;
    private int length;
    @ManyToMany
    private List<Problem> problems;
    @ManyToOne
    private Group group;
//    @OneToMany
//    private List<Standing> standing; // TODO

}
