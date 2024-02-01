package com.xjudge.mappers;

import com.xjudge.entity.Contest;
import com.xjudge.entity.ContestProblem;
import com.xjudge.entity.UserContest;
import com.xjudge.model.contest.ContestDataResp;
import com.xjudge.model.contest.ContestModel;
import com.xjudge.model.contest.ContestProblemData;
import com.xjudge.model.user.UserContestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ContestMapper {

    @Mappings({
            @Mapping(source = "alias", target = "alias"),
            @Mapping(source = "problemWeight" , target = "problemWeight") ,
            @Mapping(source = "code" , target = "code")
    }
    )
    ContestProblem toContestProblem(ContestProblemData data);

    @Mappings({
            @Mapping(source = "contestId" , target = "contestId") ,
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
    ContestDataResp toContestDataResp(Contest contest);

    UserContestModel toUserContestModel(UserContest userContest);
    UserContest toUserContest(UserContestModel userContestModel);

    Contest toContest(ContestModel contestModel);
    ContestModel toModel(Contest contest);
}
