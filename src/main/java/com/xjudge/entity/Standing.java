package com.xjudge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@AllArgsConstructor
@Table(name="standings")
public class Standing {
    //user-handle
    //score
    //penalty
    //list of problem
    //contest @one to one

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "score")
    private int score;
    @Column(name = "penality")
    private int penalty;
    //list of problem

}
