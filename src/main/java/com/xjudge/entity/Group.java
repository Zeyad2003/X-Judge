package com.xjudge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@Entity
@AllArgsConstructor
@Table(name="groups")
public class Group {

    //user-id @many to many ✅
    //contest-id @one to many ✅
    //roles => leader, manager, member
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @ManyToMany
    List<User> users;
    @OneToMany(mappedBy = "group")
    List<Contest> contests;

}
