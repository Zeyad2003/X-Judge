//package com.xjudge.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.List;
//
//@NoArgsConstructor
//@Data
//@Entity
//@AllArgsConstructor
//@Table(name="groups")
//public class Group {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "group_id")
//    private int groupId;
//    @Column(name = "group_name")
//    private String groupName;
//    @ManyToMany
//    @JoinTable(
//            name = "users_groups",
//            joinColumns = @JoinColumn(name = "group_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id")
//    )
//    List<User> users;
//    @OneToMany(mappedBy = "group")
//    List<Contest> contests;
//}
