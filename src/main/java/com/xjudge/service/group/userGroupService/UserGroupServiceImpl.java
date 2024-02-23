package com.xjudge.service.group.userGroupService;

import com.xjudge.entity.Group;
import com.xjudge.entity.User;
import com.xjudge.entity.UserGroup;
import com.xjudge.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserGroupServiceImpl implements UserGroupService {

    private final UserGroupRepository userGroupRepository;
    @Override
    public boolean existsByUserAndGroup(User user, Group group) {
        return userGroupRepository.existsByUserAndGroup(user, group);
    }

    @Override
    public Optional<UserGroup> findByUserAndGroup(User user, Group group) {
        return userGroupRepository.findByUserAndGroup(user, group);
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
