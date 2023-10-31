package com.xjudge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@AllArgsConstructor
@Table(name = "rating")
public class Rating {
  // problem
  // Enum

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private int id;
  private String value;
}
