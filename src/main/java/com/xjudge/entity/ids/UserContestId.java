package com.xjudge.entity.ids;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserContestId implements Serializable {
    private Long user;
    private Long contest;
}
