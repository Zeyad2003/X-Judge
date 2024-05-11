package com.xjudge.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Sample {
    @Column(columnDefinition = "LONGTEXT")
    @Lob
    private String input;
    @Column(columnDefinition = "LONGTEXT")
    @Lob
    private String output;
}
