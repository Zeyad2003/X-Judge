package com.xjudge.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xjudge.entity.Contest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserContestModel {

        @JsonIgnore
        private Contest contest;

        private UserModel user;

        private Boolean isFavorite;

}
