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
@Table(name = "sample")
public class Sample extends BaseEntity{

    private String sampleInput;

    private String sampleOutput;
}
