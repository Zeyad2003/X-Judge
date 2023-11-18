package com.xjudge.entity;

import com.xjudge.enums.UserRole;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
  @Id @Column(name = "user_id") private Long userId;

  @Id @Column(name = "group_id") private Long groupId;

  LocalDate userGroupJoinDate;

  @Enumerated(EnumType.STRING) private UserRole userRole;
}
