package com.xjudge.service.invitiation;

import com.xjudge.entity.Invitation;
import com.xjudge.exception.XJudgeException;
import com.xjudge.mapper.InvitationMapper;
import com.xjudge.model.enums.InvitationStatus;
import com.xjudge.model.invitation.InvitationModel;
import com.xjudge.repository.InvitationRepository;
import com.xjudge.service.group.GroupServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {

    private final InvitationRepository invitationRepository;
    private final InvitationMapper invitationMapper;

    @Override
    public void save(Invitation invitation) {
        invitationRepository.save(invitation);
    }

    @Override
    public Invitation findById(Long id) {
        return invitationRepository.findById(id).orElseThrow(
                () -> new XJudgeException("Invitation not found", GroupServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        );
    }

    @Override
    public List<InvitationModel> getInvitationByReceiverHandle(String handle) {
        List<Invitation> invitation = invitationRepository.getInvitationByReceiverHandleAndStatus(handle, InvitationStatus.PENDING);
        return invitation.stream().map(invitationMapper::toModel).toList();
    }
}
