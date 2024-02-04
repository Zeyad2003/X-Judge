package com.xjudge.entity;

import com.xjudge.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * <strong>UserGroup Entity</strong>
 * <p>Represents a user's data in a group. {Role, Date Joined}</p>
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_group")
public class UserGroup {
    @Id
    @Column(name = "user_id")
    @ManyToOne
    private User user;

    @Id
    @Column(name = "group_id")
    @ManyToOne
    private Group group;

    LocalDate userGroupJoinDate;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;
}
