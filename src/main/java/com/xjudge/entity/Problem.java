package com.xjudge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@AllArgsConstructor
@Table(name = "problem")
public class Problem {
  /// class  private String samples; @one to many prolem=> samples
  // numof user solved it
  // contest id
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private int id;

  private String title;
  private String problemStatement;
  private String constraints;
  private String input;
  private String output;
  private String link;
  private String note;
}
