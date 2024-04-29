package com.xjudge.mapper;

import com.xjudge.entity.Problem;
import com.xjudge.model.problem.ProblemModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProblemMapper {
    @Mapping(target = "problemHashtag", source = "problemHashtag")
    ProblemModel toModel(Problem problem, String problemHashtag);

    @Mapping(target = "problemHashtag", ignore = true)
    ProblemModel toModel(Problem problem);

    
}
