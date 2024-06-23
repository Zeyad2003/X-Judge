package com.xjudge.mapper;

import com.xjudge.entity.Problem;
import com.xjudge.model.problem.ProblemDescription;
import com.xjudge.model.problem.ProblemModel;
import com.xjudge.model.problem.ProblemsPageModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProblemMapper {
    @Mapping(target = "problemHashtag", source = "problemHashtag")
    @Mapping(target = "title", source = "alias")
    ProblemModel toModel(Problem problem, String problemHashtag, String alias);

    @Mapping(target = "problemHashtag", source = "problemHashtag")
    ProblemModel toModel(Problem problem, String problemHashtag);

    @Mapping(target = "problemHashtag", ignore = true)
    ProblemModel toModel(Problem problem);

    @Mapping(target = "solvedCount", source = "solvedCount")
    ProblemsPageModel toPageModel(Problem problem, Integer solvedCount);

    ProblemDescription toDescription(Problem problem);
}
