package com.xjudge.model.invitation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvitationRequest {
    @NotNull(message = "Receiver handle is required")
    @NotBlank(message = "Receiver handle is required")
    String receiverHandle;
    @NotNull(message = "Group id is required")
    @NotBlank(message = "Group id is required")
    Long groupId;
}
