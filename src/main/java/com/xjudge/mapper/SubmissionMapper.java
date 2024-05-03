package com.xjudge.mapper;

import com.xjudge.entity.Submission;
import com.xjudge.model.submission.SubmissionModel;
import com.xjudge.model.submission.SubmissionPageModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubmissionMapper {
    @Mapping(target = "contestId", source = "contest.id")
    @Mapping(target = "userHandle", source = "user.handle")
    @Mapping(target = "problemCode", source = "problem.problemCode")
    SubmissionModel toModel(Submission submission);
    List<SubmissionModel> toModels(List<Submission> submissions);
    @Mapping(target = "problemCode", source = "problemCode")
    @Mapping(target = "userHandle", source = "userHandle")
    SubmissionPageModel toPageModel(Submission submission, String problemCode, String userHandle);
}
