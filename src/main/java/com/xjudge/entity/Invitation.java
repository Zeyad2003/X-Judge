package com.xjudge.entity;

import com.xjudge.enums.InvitationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    User sender;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    User receiver;
    @ManyToOne
    @JoinColumn(name = "group_id")
    Group group;
    InvitationStatus status;
}
