package com.xjudge.entity;

import com.xjudge.model.enums.UserGroupRole;
import com.xjudge.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * <strong>UserGroup Entity</strong>
 * <p>Represents a user's data in a group. {Role, Date Joined}</p>
 */
@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_group")
public class UserGroup extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    LocalDate joinDate;

    @Enumerated(EnumType.STRING)
    private UserGroupRole role;
}
