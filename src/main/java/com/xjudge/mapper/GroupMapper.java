package com.xjudge.mapper;

import com.xjudge.entity.Contest;
import com.xjudge.entity.Group;
import com.xjudge.model.group.GroupContestModel;
import com.xjudge.model.group.GroupModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    GroupModel toModel(Group group);
    GroupModel toModel(Group group, int members);
    GroupModel toModel(Group group, int members, boolean isMember, boolean isLeader, String memberHandle);
    Group toEntity (GroupModel groupModel);
    GroupContestModel toGroupContestModel(Contest contest);

}
