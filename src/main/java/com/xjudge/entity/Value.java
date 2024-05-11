package com.xjudge.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "value_table")
public class Value {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String format;
    @Column(columnDefinition = "LONGTEXT")
    @Lob
    private String content;
}
