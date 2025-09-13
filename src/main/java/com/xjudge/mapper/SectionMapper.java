package com.xjudge.mapper;

import org.mapstruct.Mapper;

import com.xjudge.entity.problem.Section;
import com.xjudge.model.problem.SectionDto;

@Mapper(componentModel = "spring")
public interface SectionMapper {

    SectionDto toDto(Section section);

}
