package com.xjudge.mappers;

import com.xjudge.entity.Submission;
import com.xjudge.model.submission.SubmissionModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubmissionMapper {
    Submission toEntity (SubmissionModel model);
    SubmissionModel toModel(Submission submission);
}
