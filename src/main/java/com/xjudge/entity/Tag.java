package com.xjudge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <strong>Tag entity.</strong>
 * <p>Tag entity holds the tags of the problems.</p>
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long tagId;

  private String tagName;
}
