package com.xjudge.service.email.util;

import com.xjudge.entity.UserContest;
import com.xjudge.model.contest.ContestRankModel;

import java.util.Comparator;

public class ContestantComparator implements Comparator<ContestRankModel> {
    @Override
    public int compare(ContestRankModel t1, ContestRankModel t2) {
        if(t2.getNumOfAccepted() - t1.getNumOfAccepted() == 0){
            if(t1.getUserContestScore() - t2.getUserContestScore() == 0){
                return (int)(t1.getUserContestPenalty() - t2.getUserContestPenalty());
            }
            return t2.getUserContestScore() - t1.getUserContestScore();
        }
        return t2.getNumOfAccepted() - t1.getNumOfAccepted();
    }
}
