package com.xjudge.mapper;

import com.xjudge.entity.Contest;
import com.xjudge.entity.ContestProblem;
import com.xjudge.entity.User;
import com.xjudge.entity.UserContest;
import com.xjudge.exception.XJudgeException;
import com.xjudge.model.contest.ContestModel;
import com.xjudge.model.contest.ContestPageModel;
import com.xjudge.model.contest.ContestProblemModel;
import com.xjudge.model.contest.modification.ContestClientRequest;
import com.xjudge.model.enums.ContestStatus;
import com.xjudge.model.enums.ContestVisibility;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.http.HttpStatus;

@Mapper(componentModel = "spring")
public interface ContestMapper {

    @Mapping(target = "duration", expression = "java(java.time.Duration.ofSeconds(contestModel.getDurationSeconds()))")
    @Mapping(target = "password", expression = "java(passwordValidation(contestModel))")
    Contest toContestClassical(ContestClientRequest contestModel);

    @Mapping(target = "duration", expression = "java(java.time.Duration.ofSeconds(contestModel.getDurationSeconds()))")
    @Mapping(target = "password", ignore = true)
    Contest toContestGroup(ContestClientRequest contestModel);

    @Mapping(target = "numberOfParticipants" , expression = "java(getNumberOfParticipants(contest))")
    @Mapping(target = "groupName" , source = "contest.group.name")
    @Mapping(target = "groupId" , source = "contest.group.id")
    @Mapping(target = "handle" , source = "owner.handle")
    @Mapping(target = "photoUrl" , source = "owner.photoUrl")
    @Mapping(target = "ownerId" , source = "owner.id")
    @Mapping(target = "id" , source = "contest.id")
    ContestPageModel toContestPageModel(Contest contest , User owner);

    @Mapping(target = "endTime" , expression = "java(contest.getBeginTime().plus(contest.getDuration()))")
    @Mapping(target = "ownerHandle" , source = "owner.handle")
    @Mapping(target = "ownerId" , source = "owner.id")
    @Mapping(target = "groupName" , source = "contest.group.name")
    @Mapping(target = "groupId" , source = "contest.group.id")
    @Mapping(target = "id" , source = "contest.id")
    ContestModel toContestModel(Contest contest , User owner , ContestStatus contestStatus);

    @Mapping(target = "source" , source = "contestProblem.problem.source")
    @Mapping(target = "problemLink" , source = "contestProblem.problem.problemLink")
    ContestProblemModel toContestProblemModel(ContestProblem contestProblem);


    default java.lang.String passwordValidation(ContestClientRequest contestModel) {
        if (contestModel.getVisibility() == ContestVisibility.PRIVATE && (contestModel.getPassword() == null || contestModel.getPassword().isEmpty())) {
            throw new XJudgeException("Password is REQUIRED when creating a private contest", ContestMapper.class.getName(), HttpStatus.BAD_REQUEST);
        } else if (contestModel.getVisibility() == ContestVisibility.PUBLIC) {
            return null;
        }
        return contestModel.getPassword();
    }

    default Long getNumberOfParticipants(Contest contest){
        return contest.getUsers()
                .stream()
                .filter(UserContest::getIsParticipant)
                .count();
    }


}

