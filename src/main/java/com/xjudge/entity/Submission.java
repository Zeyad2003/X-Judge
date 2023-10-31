package com.xjudge.entity;

import jakarta.persistence.*;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@AllArgsConstructor
@Table(name = "submission")
public class Submission {
  // user-id
  // problem-id
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private int id;
  private String code;
  private String language; // Enum
  private Date date;       // current time
  private String result;
}
