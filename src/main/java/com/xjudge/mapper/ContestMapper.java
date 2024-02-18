package com.xjudge.mapper;

import com.xjudge.entity.Contest;
import com.xjudge.entity.ContestProblem;
import com.xjudge.entity.UserContest;
import com.xjudge.model.contest.ContestCreationModel;
import com.xjudge.model.contest.ContestData;
import com.xjudge.model.contest.ContestModel;
import com.xjudge.model.user.UserContestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ContestMapper {

    Contest toContest(ContestCreationModel contestCreationModel);
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
    ContestData toContestDataResp(Contest contest);

    UserContestModel toUserContestModel(UserContest userContest);
    UserContest toUserContest(UserContestModel userContestModel);

    Contest toContest(ContestModel contestModel);
    ContestModel toModel(Contest contest);*/
}
