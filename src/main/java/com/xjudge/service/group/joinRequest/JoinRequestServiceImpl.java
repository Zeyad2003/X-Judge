package com.xjudge.service.group.joinRequest;

import com.xjudge.entity.JoinRequest;
import com.xjudge.exception.XJudgeException;
import com.xjudge.repository.JoinRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinRequestServiceImpl implements JoinRequestService {

    private final JoinRequestRepository joinRequestRepository;

    @Override
    public void save(JoinRequest joinRequest) {
        this.joinRequestRepository.save(joinRequest);
    }

    @Override
    public JoinRequest findById(Long id) {
        return this.joinRequestRepository.findById(id).orElseThrow(
                () -> new XJudgeException(
                        "Join request not found",
                        JoinRequestServiceImpl.class.getName(),
                        HttpStatus.NOT_FOUND)
        );
    }
}
