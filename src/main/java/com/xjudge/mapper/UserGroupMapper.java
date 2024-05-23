package com.xjudge.mapper;

import com.xjudge.entity.UserGroup;
import com.xjudge.model.group.GroupMemberModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserGroupMapper {
    @Mapping(target = "name", expression = "java(userGroup.getUser().getFirstName() + ' ' + userGroup.getUser().getLastName())")
    @Mapping(target = "handle", source = "user.handle")
    GroupMemberModel toGroupMemberModel(UserGroup userGroup);
}
