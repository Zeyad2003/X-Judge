package com.xjudge.service.invitiation;

import com.xjudge.entity.Invitation;

public interface InvitationService {
    void save(Invitation invitation);
    Invitation findById(Long id);
}
