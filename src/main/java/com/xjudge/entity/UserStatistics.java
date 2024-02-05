package com.xjudge.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_statistics")
public class UserStatistics extends BaseEntity{

    Long userSolvedCount;

    Long userAttemptedCount;

}
