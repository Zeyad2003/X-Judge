package com.xjudge.mapper;

import org.mapstruct.Mapper;

import com.xjudge.entity.problem.SampleTestCase;
import com.xjudge.model.problem.SampleTestCaseDto;

@Mapper(componentModel = "spring")
public interface SampleTestCaseMapper {

    SampleTestCaseDto toDto(SampleTestCase sampleTestCase);
}
