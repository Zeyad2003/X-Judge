package com.xjudge.mapper;

import com.xjudge.entity.Group;
import com.xjudge.model.group.GroupModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    GroupModel toModel(Group group);
    Group toEntity (GroupModel groupModel);
}
