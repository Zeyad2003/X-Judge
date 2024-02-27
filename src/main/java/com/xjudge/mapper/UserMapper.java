package com.xjudge.mapper;

import com.xjudge.entity.User;
import com.xjudge.model.user.UserModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserModel toModel (User user);
    User toEntity (UserModel model);
}