package com.xjudge.service.group.userGroupService;

import com.xjudge.entity.Group;
import com.xjudge.entity.User;
import com.xjudge.entity.UserGroup;
import com.xjudge.exception.XJudgeException;
import com.xjudge.model.enums.UserGroupRole;
import com.xjudge.repository.UserGroupRepository;
import com.xjudge.service.group.GroupServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserGroupServiceImpl implements UserGroupService {

    private final UserGroupRepository userGroupRepository;
    @Override
    public boolean existsByUserAndGroup(User user, Group group) {
        return userGroupRepository.existsByUserAndGroup(user, group);
    }

    @Override
    public UserGroup findByUserAndGroup(User user, Group group) {
        return userGroupRepository.findByUserAndGroup(user, group).orElseThrow(
                () -> new XJudgeException("User not found in group.", GroupServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        );
    }

    @Override
    public UserGroup findByUserHandleAndGroupId(String userHandle, Long groupId) {
        return userGroupRepository.findByUserHandleAndGroupId(userHandle, groupId).orElseThrow(
                () -> new XJudgeException("User not found in group.", GroupServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        );
    }

    @Override
    public List<UserGroup> findAllByUserAndRole(User user, UserGroupRole role) {
        return userGroupRepository.findAllByUserAndRole(user, role);
    }


    @Override
    public void save(UserGroup userGroup) {
        userGroupRepository.save(userGroup);
    }

    @Override
    public void delete(UserGroup userGroup) {
        userGroupRepository.delete(userGroup);
    }

    @Override
    public void deleteById(Long id) {
        userGroupRepository.deleteById(id);
    }
}
