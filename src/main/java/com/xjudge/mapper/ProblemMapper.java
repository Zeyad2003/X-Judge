package com.xjudge.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.xjudge.entity.problem.Problem;
import com.xjudge.model.problem.ProblemDetails;

@Mapper(componentModel = "spring", uses = {SampleTestCaseMapper.class, SectionMapper.class, PropertyMapper.class})
public interface ProblemMapper {

    ProblemDetails toDto(Problem problem);

    void updateProblemFromSource(Problem source, @MappingTarget Problem target);
}
