package com.xjudge.mapper;

import com.xjudge.entity.Invitation;
import com.xjudge.model.invitation.InvitationModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InvitationMapper {
    @Mapping(target = "senderHandle", source = "invitation.sender.handle")
    @Mapping(target = "receiverHandle", source = "invitation.receiver.handle")
    @Mapping(target = "groupName", source = "invitation.group.name")
    InvitationModel toModel(Invitation invitation);
    Invitation toEntity(InvitationModel invitationModel);
}
