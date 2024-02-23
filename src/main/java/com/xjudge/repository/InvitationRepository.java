package com.xjudge.repository;

import com.xjudge.entity.Invitation;
import com.xjudge.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    List<Invitation> getInvitationsByReceiver(User receiver);
}
