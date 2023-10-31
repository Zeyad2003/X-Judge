package com.xjudge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@AllArgsConstructor
@Table(name="samples")
public class Samples {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String input;
    private String output;
    private String notes;

}
