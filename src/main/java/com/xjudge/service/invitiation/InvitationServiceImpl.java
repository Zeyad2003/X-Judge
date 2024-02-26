package com.xjudge.service.invitiation;

import com.xjudge.entity.Invitation;
import com.xjudge.exception.XJudgeException;
import com.xjudge.repository.InvitationRepository;
import com.xjudge.service.group.groupServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {

    private final InvitationRepository invitationRepository;

    @Override
    public void save(Invitation invitation) {
        invitationRepository.save(invitation);
    }

    @Override
    public Invitation findById(Long id) {
        return invitationRepository.findById(id).orElseThrow(
                () -> new XJudgeException("Invitation not found", groupServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        );
    }
}
