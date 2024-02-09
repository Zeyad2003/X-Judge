package com.xjudge.mappers;

import com.xjudge.entity.Problem;
import com.xjudge.model.problem.ProblemModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProblemMapper {
    ProblemModel toModel(Problem problem);
}
