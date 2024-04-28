package com.xjudge.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xjudge.entity.key.UserGroupKey;
import com.xjudge.model.enums.UserGroupRole;
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
public class UserGroup extends BaseEntity<UserGroupKey> {

    @EmbeddedId
    UserGroupKey id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    @MapsId("userId")
    @ToString.Exclude
    @JsonIgnore
    private User user;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "group_id")
    @MapsId("groupId")
    @ToString.Exclude
    @JsonIgnore
    private Group group;

    LocalDate joinDate;

    @Enumerated(EnumType.STRING)
    private UserGroupRole role;
}
