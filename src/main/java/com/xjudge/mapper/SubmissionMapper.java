package com.xjudge.mapper;

import com.xjudge.entity.Submission;
import com.xjudge.model.contest.ContestStatusPageModel;
import com.xjudge.model.submission.OpenSubmissionModel;
import com.xjudge.model.submission.SubmissionModel;
import com.xjudge.model.submission.SubmissionPageModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubmissionMapper {
    @Named("toModel")
    @Mapping(target = "contestId", source = "contest.id")
    @Mapping(target = "userHandle", source = "user.handle")
    @Mapping(target = "problemCode", source = "problem.code")
    SubmissionModel toModel(Submission submission);

    @Named("toOpenSubmissionModel")
    @Mapping(target = "contestId", source = "submission.contest.id")
    @Mapping(target = "userHandle", source = "submission.user.handle")
    @Mapping(target = "problemCode", source = "submission.problem.code")
    @Mapping(target = "solution" , source = "solution")
    OpenSubmissionModel toOpenSubmissionModel(Submission submission , String solution);

    List<SubmissionModel> toModels(List<Submission> submissions);

    @Mapping(target = "problemCode", source = "problemCode")
    @Mapping(target = "userHandle", source = "userHandle")
    SubmissionPageModel toPageModel(Submission submission, String problemCode, String userHandle);

    @Mapping(target = "problemCode", source = "submission.problem.code")
    @Mapping(target = "userHandle", source = "submission.user.handle")
    ContestStatusPageModel toContestStatusPageModel(Submission submission , String problemHashtag);
}
