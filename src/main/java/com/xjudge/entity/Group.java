package com.xjudge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@AllArgsConstructor
@Table(name = "groups")
public class Group {

  // user-id @many to many
  // contes-id @onetomany
  // roles => leader, manager, member
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private int id;
  private String name;
}
