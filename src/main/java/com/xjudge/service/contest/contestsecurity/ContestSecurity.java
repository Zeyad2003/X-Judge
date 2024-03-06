package com.xjudge.service.contest.contestsecurity;

import com.xjudge.entity.Contest;
import com.xjudge.entity.Group;
import com.xjudge.entity.User;
import com.xjudge.exception.XJudgeException;
import com.xjudge.mapper.UserMapper;
import com.xjudge.model.enums.ContestType;
import com.xjudge.model.enums.ContestVisibility;
import com.xjudge.repository.ContestRepo;
import com.xjudge.repository.UserContestRepo;
import com.xjudge.service.contest.ContestService;
import com.xjudge.service.group.groupSecurity.GroupSecurity;
import com.xjudge.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ContestSecurity {

    private final GroupSecurity groupSecurity;
    private final ContestService contestService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final ContestRepo contestRepo;
    private final UserContestRepo userContestRepo;


    public boolean authorizeCreateContest(String handle , Long groupId , ContestType type){
        if(type == ContestType.GROUP){
            return isUserAuthorizedInGroup(handle , groupId);
        }
        return true;
    }

    public boolean authorizeContestantsRoles(String handle, Long id , String password){
        Contest contest = contestService.getContest(id);
        if(!isContestOwner(contest, handle)
                &&!isContestParticipant(contest , handle)
                && contest.getType() == ContestType.CLASSIC
                && contest.getVisibility() == ContestVisibility.PRIVATE){

            if(isPasswordCorrect(contest.getPassword() , password)) {
                User user = userMapper.toEntity(userService.findByHandle(handle));
                contestService.handleContestUserRelation(user, contest, false, true);
                contestRepo.save(contest);
            }
        }
        else if(contest.getType() == ContestType.GROUP && contest.getVisibility() == ContestVisibility.PRIVATE){
            Group group = contest.getGroup();
            return groupSecurity.isMember(handle, group.getId());
        }
        return true;
    }

    public boolean authorizeDelete(String handle  , Long contestId){
        Contest contest = contestService.getContest(contestId);
        if(contest.getType() == ContestType.GROUP){
            Group group = contest.getGroup();
            return isUserAuthorizedInGroup(handle , group.getId());
        }
        else if(contest.getType() == ContestType.CLASSIC){
             if(!isContestOwner(contest, handle)){
                 throw new XJudgeException("UnAuthorized owner" , ContestSecurity.class.getName() , HttpStatus.FORBIDDEN);
             }
        }
        return true;
    }

    public boolean authorizeUpdate(String handle  , Long contestId , ContestType contestType , Long groupId){
        Contest contest = contestService.getContest(contestId);
        if(contestType == ContestType.GROUP){
            if(contest.getType() == ContestType.CLASSIC){
                if(!isContestOwner(contest, handle)){
                    throw new XJudgeException("UnAuthorized owner" , ContestSecurity.class.getName() , HttpStatus.FORBIDDEN);
                }
            }
            return isUserAuthorizedInGroup(handle , groupId) && isUserAuthorizedInGroup(handle , contest.getGroup().getId());
        }
        else if(contestType == ContestType.CLASSIC){
            if(!isContestOwner(contest, handle)){
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

    private boolean isContestOwner(Contest contest , String handle){
        return contest.getUsers()
                .stream()
                .anyMatch(userContest -> userContest.getUser().getHandle().equals(handle)
                        && userContest.getIsOwner());
    }

    private boolean isUserAuthorizedInGroup(String handle , Long groupId){
        return groupSecurity.isMember(handle, groupId) &&
                groupSecurity.hasAnyRole(handle , groupId , List.of("LEADER" , "MANAGER"));
    }

    private boolean isContestParticipant(Contest contest , String handle){
        return contest.getUsers()
                .stream()
                .anyMatch(userContest -> userContest.getUser().getHandle().equals(handle)
                        && userContest.getIsParticipant());
    }
}
