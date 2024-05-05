package com.xjudge.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xjudge.model.enums.GroupVisibility;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * <strong>Group Entity</strong>
 * <p>Represents a group of users and the contests they are participating in with roles for those users.</p>
 */
@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="`group`")
public class Group extends BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    LocalDate creationDate;

    @Enumerated(EnumType.STRING)
    GroupVisibility visibility;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.PERSIST , mappedBy = "group")
    @ToString.Exclude
    List<Contest> groupContests;

    @JsonIgnore
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<UserGroup> groupUsers;


    String leaderHandle;


}
