package com.xjudge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@AllArgsConstructor
@Table(name="standing")
public class Standing {
    //user-handle
    //score
    //penalty
    //list of problem
    //contest @one to one

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int score;
    private int penalty;
    //list of problem

}
