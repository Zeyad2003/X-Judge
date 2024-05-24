package com.xjudge.model.group;

import com.xjudge.model.enums.UserGroupRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberModel {
    private String name;
    private String handle;
    private UserGroupRole role;
    private LocalDate joinDate;
}
