package com.xjudge.mapper;

import com.xjudge.entity.Contest;
import com.xjudge.exception.XJudgeException;
import com.xjudge.model.contest.modification.ContestClientRequest;
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

    default String passwordValidation(ContestClientRequest contestModel) {
        if (contestModel.getVisibility() == ContestVisibility.PRIVATE && (contestModel.getPassword() == null || contestModel.getPassword().isEmpty())) {
            throw new XJudgeException("Password is REQUIRED when creating a private contest", ContestMapper.class.getName(), HttpStatus.BAD_REQUEST);
        } else if (contestModel.getVisibility() == ContestVisibility.PUBLIC) {
            return null;
        }
        return contestModel.getPassword();
    }
}
