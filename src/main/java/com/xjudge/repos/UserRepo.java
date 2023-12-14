package com.xjudge.repos;

import com.xjudge.entity.User;
import org.mapstruct.control.MappingControl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User , Long> {
    Optional<User> findUserByUserHandle(String userHandle);
}
