package com.xjudge.mapper;

import com.xjudge.entity.Submission;
import com.xjudge.model.submission.SubmissionModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubmissionMapper {
    @Mapping(target = "contestId", source = "contest.id")
    @Mapping(target = "userHandle", source = "user.handle")
    @Mapping(target = "problemCode", source = "problem.problemCode")
    SubmissionModel toModel(Submission submission);
}
