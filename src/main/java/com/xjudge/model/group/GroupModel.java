package com.xjudge.model.group;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xjudge.entity.Contest;
import com.xjudge.entity.UserGroup;
import com.xjudge.model.enums.GroupVisibility;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupModel {
    private Long id;

    private String name;

    private String description;

    LocalDate creationDate;

    GroupVisibility visibility;

    @JsonIgnore
    List<Contest> groupContests;

    @JsonIgnore
    List<UserGroup> groupUsers;

    String leaderHandle;
}
