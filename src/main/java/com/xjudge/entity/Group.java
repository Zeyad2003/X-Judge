package com.xjudge.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.xjudge.enums.GroupVisibility;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * <strong>Group Entity</strong>
 * <p>Represents a group of users and the contests they are participating in with roles for those users.</p>
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="`group`")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "groupId")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long groupId;

    private String groupName;

    @Column(columnDefinition = "TEXT")
    private String groupDescription;

    LocalDate groupCreationDate;

    @Enumerated(EnumType.STRING)
    GroupVisibility groupVisibility;

    @OneToMany
    @JoinColumn(name = "group_id")
    List<Contest> groupContests;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_group",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> groupUsers;
}
