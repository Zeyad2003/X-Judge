package com.xjudge.mapper;

import org.mapstruct.Mapper;

import com.xjudge.entity.problem.Property;
import com.xjudge.model.problem.PropertyDto;

@Mapper(componentModel = "spring")
public interface PropertyMapper {

    PropertyDto toDto(Property property);
}
