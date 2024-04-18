package com.xjudge.mapper;

import com.xjudge.entity.Submission;
import com.xjudge.entity.UserContest;
import com.xjudge.model.contest.ContestRankModel;
import com.xjudge.model.contest.ContestRankSubmission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserContestMapper {

    @Mappings(
            value = {
                    @Mapping(source = "userContest.user.handle" , target = "handle"),
                    @Mapping(source = "userContest.user.id" , target = "userId"),
                    @Mapping(source = "userContest.user.photoUrl" , target = "photoUrl"),
                    @Mapping(source = "userContest.user.firstName" , target = "firstName"),
                    @Mapping(source = "userContest.user.lastName" , target = "lastName"),
            }

    )
    ContestRankModel toContestRankModel(UserContest userContest , List<ContestRankSubmission>submissionList);

    @Mappings(
            {
                    @Mapping(source = "submission.id" , target = "submissionId") ,
                    @Mapping(source = "submission.verdict" , target = "status") ,
                    @Mapping(source = "submission.memoryUsage" , target = "memory") ,
                    @Mapping(source = "submission.timeUsage" , target = "time") ,
                    @Mapping(source = "problemHashtag" , target = "problemIndex") ,
                    @Mapping(target = "submitTime" , expression = "java(getSubmitTimeInContest(contestBeginTime , submitTime))")
            }
    )
    ContestRankSubmission toContestRankSubmissionModel(Submission submission , String problemHashtag , Instant contestBeginTime , Instant submitTime);

     default Long getSubmitTimeInContest(Instant contestBeginTime , Instant submitTime){
        return Duration.between(contestBeginTime , submitTime)
                .getSeconds();
    }
}
