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
@Table(name="contest")
public class Contest {
    //id
    //name
    //begintime
    //length
    //standang @one to one
    //problems @many to many
    //button stutus for all submission
    //group id @many to one

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private Date begintime;
    private int length;


}
