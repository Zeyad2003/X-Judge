package com.xjudge.entity;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@AllArgsConstructor
@Table(name = "blog")
public class Blog {
  // userid

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private int id;
  private String title;
  private Date time;
  private String text;
}
