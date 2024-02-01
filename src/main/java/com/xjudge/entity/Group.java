package com.xjudge.entity;

import com.xjudge.enums.GroupVisibility;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
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

    public void addUser(User user) {
        if (groupUsers == null) {
            groupUsers = new ArrayList<>();
        }
        groupUsers.add(user);
    }

    public void deleteUser(User user) {
        groupUsers.removeIf(u -> u.equals(user));
    }
}
