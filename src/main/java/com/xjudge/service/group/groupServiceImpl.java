package com.xjudge.service.group;

import com.xjudge.entity.Contest;
import com.xjudge.entity.Group;
import com.xjudge.entity.User;
import com.xjudge.model.group.GroupRequest;
import com.xjudge.repository.GroupRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class groupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    @Override
    public List<Group> publicGroups() {
        return null;
    }

    @Override
    public Group create(GroupRequest groupRequest) {
        return null;
    }

    @Override
    public Group details(Long groupId) {
        return null;
    }

    @Override
    public Group update(Long groupId, GroupRequest groupRequest) {
        return null;
    }

    @Override
    public void delete(Long groupId) {

    }

    @Override
    public void addContest(Long contestId, Long groupId) {

    }

    @Override
    public void inviteUser(Long groupId, Long userId) {

    }

    @Override
    public void join(Long groupId, Long userId) {

    }

    @Override
    public void leave(Long groupId, Long userId) {

    }

    @Override
    public List<Contest> Contests(Long groupId) {
        return null;
    }

    @Override
    public List<User> Users(Long groupId) {
        return null;
    }

    @Override
    public boolean isPublic(Long groupId) {
        return false;
    }

    @Override
    public boolean isPrivate(Long groupId) {
        return false;
    }
}
