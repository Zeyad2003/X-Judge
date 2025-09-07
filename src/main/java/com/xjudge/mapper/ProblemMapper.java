package com.xjudge.mapper;

import com.xjudge.entity.Problem;
import com.xjudge.model.problem.ProblemDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProblemMapper {

    ProblemDetails toDetails(Problem problem);

    @Mapping(target = "id", ignore = true)
    Problem toEntity(ProblemDetails details);
}
