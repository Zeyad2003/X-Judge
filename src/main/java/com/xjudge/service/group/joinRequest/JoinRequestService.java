package com.xjudge.service.group.joinRequest;

import com.xjudge.entity.JoinRequest;

public interface JoinRequestService {
    void save(JoinRequest joinRequest);
    JoinRequest findById(Long id);
}
