package com.xjudge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Data
@Entity
@AllArgsConstructor
@Table(name="users")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "password")
    private String password;
    @Column(name = "handle", unique = true)
    private String handle;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "codeforces_handle")
    private String codeforcesHandle;
    @Column(name = "atcoder_handle")
    private String atcoderHandle;
    @Column(name = "photo_url")
    private String photoUrl;
    @OneToMany(mappedBy = "user")
    private List<Submission> submissions;
    @ManyToMany(mappedBy = "users")
    private List<Group> groups;


}
