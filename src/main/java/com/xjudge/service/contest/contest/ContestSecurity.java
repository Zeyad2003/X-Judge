package com.xjudge.service.contest.contest;

import com.xjudge.entity.Contest;
import com.xjudge.entity.Group;
import com.xjudge.entity.User;
import com.xjudge.entity.UserContest;
import com.xjudge.exception.XJudgeException;
import com.xjudge.model.enums.ContestType;
import com.xjudge.model.enums.ContestVisibility;
import com.xjudge.service.contest.ContestService;
import com.xjudge.service.group.groupSecurity.GroupSecurity;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@AllArgsConstructor
public class ContestSecurity {

    private final GroupSecurity groupSecurity;
    private final ContestService contestService;


    public boolean authorizeGroupContest(String handle , Long groupId , ContestType type){
        if(type == ContestType.GROUP){
            return isUserAuthorizedInGroup(handle , groupId);
        }
        return true;
    }

    public boolean authorizeGetContest(String handle , Long id , String password){
        Contest contest = contestService.getContest(id);
        if(contest.getType() == ContestType.CLASSIC && contest.getVisibility() == ContestVisibility.PRIVATE){
           return isPasswordCorrect(contest.getPassword() , password);
        }
        else if(contest.getType() == ContestType.GROUP && contest.getVisibility() == ContestVisibility.PRIVATE){
            Group group = contest.getGroup();
            return groupSecurity.isMember(handle , group.getId());
        }
        return true;
    }

    public boolean authorizeUpdateGroup(String handle, Long groupId , ContestType type , Long contestId){
        if(type == ContestType.GROUP){
            return isUserAuthorizedInGroup(handle , groupId);
        }
        else if(type == ContestType.CLASSIC){
             Contest contest = contestService.getContest(contestId);
             if(!isContestOwner(handle , contest)){
                 throw new XJudgeException("UnAuthorized owner" , ContestSecurity.class.getName() , HttpStatus.FORBIDDEN);
             }
        }
        return true;
    }

    private boolean isPasswordCorrect(String contestPassword , String requestPassword){
        if(!contestPassword.equals(requestPassword)){
            throw new XJudgeException("Password is not correct" , ContestSecurity.class.getName() , HttpStatus.FORBIDDEN);
        }
        return true;
    }

    private boolean isContestOwner(String handle , Contest contest){
        return contest.getUsers()
                .stream()
                .anyMatch(userContest -> userContest.getUser().getHandle().equals(handle)
                        && userContest.getIsOwner());
    }

    private boolean isUserAuthorizedInGroup(String handle , Long groupId){
        return groupSecurity.isMember(handle, groupId) &&
                groupSecurity.hasAnyRole(handle , groupId , List.of("LEADER" , "MANAGER"));
    }
}
