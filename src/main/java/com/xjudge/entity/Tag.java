package com.xjudge.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Tag extends BaseEntity {

    private String tagName;
}
