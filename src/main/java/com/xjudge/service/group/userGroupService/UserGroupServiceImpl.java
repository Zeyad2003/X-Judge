package com.xjudge.service.group.userGroupService;

import com.xjudge.entity.Group;
import com.xjudge.entity.User;
import com.xjudge.entity.UserGroup;
import com.xjudge.exception.SubmitException;
import com.xjudge.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
                () -> new SubmitException("User not found in group.", HttpStatus.NOT_FOUND)
        );
    }

    @Override
    public UserGroup findByUserHandleAndGroupId(String userHandle, Long groupId) {
        return userGroupRepository.findByUserUserHandleAndGroupGroupId(userHandle, groupId).orElseThrow(
                () -> new SubmitException("User not found in group.", HttpStatus.NOT_FOUND)
        );
    }

    @Override
    public void save(UserGroup userGroup) {
        userGroupRepository.save(userGroup);
    }

    @Override
    public void deleteById(Long id) {
        userGroupRepository.deleteById(id);
    }
}
