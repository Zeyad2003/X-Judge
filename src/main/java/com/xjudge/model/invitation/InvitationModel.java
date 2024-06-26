package com.xjudge.model.invitation;

import com.xjudge.model.enums.InvitationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvitationModel {
    Long id;
    String senderHandle;
    String receiverHandle;
    String groupName;
    InvitationStatus status;
    LocalDate date;
}
