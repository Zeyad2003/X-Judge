package com.xjudge.service.invitiation;

import com.xjudge.entity.Invitation;
import com.xjudge.repository.InvitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {

    private final InvitationRepository invitationRepository;

    @Override
    public void save(Invitation invitation) {
        invitationRepository.save(invitation);
    }
}
