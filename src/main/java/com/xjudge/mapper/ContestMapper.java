package com.xjudge.mapper;

import com.xjudge.entity.Contest;
import com.xjudge.model.contest.ContestBaseModel;
import com.xjudge.model.enums.ContestVisibility;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContestMapper {

    @Mapping(target = "duration", expression = "java(java.time.Duration.ofSeconds(contestModel.getDurationSeconds()))")
    @Mapping(target = "password", expression = "java(passwordValidation(contestModel))")
    Contest toContest(ContestBaseModel contestModel);

    default String passwordValidation(ContestBaseModel contestModel) {
        if (contestModel.getVisibility() == ContestVisibility.PRIVATE && (contestModel.getPassword() == null || contestModel.getPassword().isEmpty())) {
            throw new IllegalArgumentException("PASSWORD_REQUIRED");
        }
        return contestModel.getPassword();
    }

/*
    @Mappings({
            @Mapping(source = "id" , target = "contestId") ,
            @Mapping(source = "contestLength" , target = "contestLength") ,
            @Mapping(source = "contestTitle" , target = "contestTitle") ,
            @Mapping(source = "contestType" , target = "contestType") ,
            @Mapping(source = "contestVisibility" , target = "contestVisibility") ,
            @Mapping(source = "contestDescription" , target = "contestDescription") ,
            @Mapping(source = "contestBeginTime" , target = "contestBeginTime") ,
            @Mapping(source = "contestUsers" , target = "participants") ,
            @Mapping(source = "contestSubmissions" , target = "submissions") ,
    }
    )
    ContestUpdatingModel toContestDataResp(Contest contest);

    UserContestModel toUserContestModel(UserContest userContest);
    UserContest toUserContest(UserContestModel userContestModel);

    Contest toContest(ContestUserDataModel contestModel);
    ContestUserDataModel toModel(Contest contest);*/
}
