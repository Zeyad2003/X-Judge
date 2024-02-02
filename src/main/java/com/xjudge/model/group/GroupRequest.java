package com.xjudge.model.group;

import com.xjudge.enums.GroupVisibility;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupRequest {
    @NotBlank(message = "Please enter a valid name")
    String name;
    String description;
    @NotBlank(message = "Please enter a valid visibility")
    GroupVisibility visibility;
}
