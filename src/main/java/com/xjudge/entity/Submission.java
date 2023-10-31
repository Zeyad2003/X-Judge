package com.xjudge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
@Entity
@AllArgsConstructor
@Table(name="submission")
public class Submission {
// user-id
// problem-id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String code;
    private String language;//Enum
    private Date date;  //current time
    private String result;



}
