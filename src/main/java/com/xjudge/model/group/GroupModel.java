package com.xjudge.model.group;

import com.xjudge.model.enums.GroupVisibility;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
}
