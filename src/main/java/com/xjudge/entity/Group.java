package com.xjudge.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xjudge.enums.GroupVisibility;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
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

    @OneToOne
    private User leader;

    @OneToMany
    @JoinColumn(name = "group_id")
    List<Contest> groupContests;

    @JsonIgnore
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<UserGroup> users;

    public void addUser(UserGroup user) {
        if (users == null) {
            users = new ArrayList<>();
        }
        users.add(user);
    }

    public void deleteUser(UserGroup user) {
        users.remove(user);
    }

    public boolean userFound(User user) {
        return users.stream().anyMatch(userGroup -> userGroup.getUser().equals(user));
    }
}
