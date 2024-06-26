package com.xjudge.service.invitiation;

import com.xjudge.entity.Invitation;
import com.xjudge.model.invitation.InvitationModel;

import java.util.List;

public interface InvitationService {
    void save(Invitation invitation);
    Invitation findById(Long id);
    List<InvitationModel> getInvitationByReceiverHandle(String handle);
}
