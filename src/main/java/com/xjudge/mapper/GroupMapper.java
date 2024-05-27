package com.xjudge.mapper;

import com.xjudge.entity.Contest;
import com.xjudge.entity.Group;
import com.xjudge.model.group.GroupContestModel;
import com.xjudge.model.group.GroupModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    GroupModel toModel(Group group);
    Group toEntity (GroupModel groupModel);
    GroupContestModel toGroupContestModel(Contest contest);

}
