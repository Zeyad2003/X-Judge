package com.xjudge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Data
@Entity
@AllArgsConstructor
@Table(name = "user")
public class User {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private int id;
  private String password;
  @Column(unique = true) private String handle;
  @Column(unique = true) private String email;
  private String codeforcesHandle;
  private String atcoderHandle;
  private String photoUrl;
}
