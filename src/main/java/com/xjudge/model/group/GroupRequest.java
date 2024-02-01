package com.xjudge.model.group;

import com.xjudge.enums.GroupVisibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupRequest {
    String name;
    String description;
    GroupVisibility visibility;
}
