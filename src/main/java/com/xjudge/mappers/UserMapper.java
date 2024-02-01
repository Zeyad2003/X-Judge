package com.xjudge.mappers;

import com.xjudge.entity.User;
import com.xjudge.entity.UserContest;
import com.xjudge.model.user.UserContestModel;
import com.xjudge.model.user.UserModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserModel toModel (User user);
    User toEntity (UserModel model);
}
