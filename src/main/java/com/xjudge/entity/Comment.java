package com.xjudge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@AllArgsConstructor
@Table(name="comments")
public class Comment {

    //user-id many to one
    //blog-id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="test")
    private String des;

}