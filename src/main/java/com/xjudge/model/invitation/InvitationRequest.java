package com.xjudge.model.invitation;

import com.xjudge.enums.InvitationStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvitationRequest {
    String token;
    Long receiverId;
    Long groupId;
    InvitationStatus status = InvitationStatus.PENDING;
}
