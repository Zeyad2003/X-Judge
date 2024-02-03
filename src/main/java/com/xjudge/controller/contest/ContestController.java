package com.xjudge.controller.contest;

import com.github.dockerjava.api.exception.BadRequestException;
import com.xjudge.model.contest.ContestCreationRequest;
import com.xjudge.model.contest.ContestDataResp;
import com.xjudge.model.contest.ContestModel;
import com.xjudge.model.problem.ContestProblemResp;
import com.xjudge.service.contest.ContestService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ContestController {

    ContestService service;


    @Autowired
    public ContestController(ContestService service) {
        this.service = service;
    }


    @GetMapping("/contests")
    public ResponseEntity<List<ContestDataResp>> createContest(){
        return new ResponseEntity<>(service.getAllContests() , HttpStatus.OK);
    }

    @GetMapping("/contests/{id}")
    public ResponseEntity<ContestDataResp> getContest(@PathVariable Long id){

        if(id <= 0) throw new BadRequestException("CONTEST_ID_LESS_THAN_ZERO");
        return new ResponseEntity<>(service.getContest(id) , HttpStatus.OK);
    }

    @PostMapping("/users/{userId}/contests")
    public ResponseEntity<ContestDataResp> createContest(@PathVariable Long userId , @Valid @RequestBody ContestCreationRequest contest){

        if(userId <= 0) throw new BadRequestException("CONTEST_ID_LESS_THAN_ZERO");
        return new ResponseEntity<>(service.createContest(userId , contest) , HttpStatus.OK);

    }

    @PutMapping("/contests/{id}")
    public ResponseEntity<ContestModel> updateContest(@PathVariable Long id , @RequestBody ContestModel model){

        if(id <= 0) throw new BadRequestException("CONTEST_ID_LESS_THAN_ZERO");
        return new ResponseEntity<>(service.updateContest(id , model) , HttpStatus.OK);

    }

    @DeleteMapping("/contests/{id}")
    public ResponseEntity<Void> deleteContest(@PathVariable Long id){

        if(id <= 0) throw new BadRequestException("CONTEST_ID_LESS_THAN_ZERO");
        service.deleteContest(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/contests/{id}/problem")
    public ResponseEntity<List<ContestProblemResp>> getContestProblems(@PathVariable Long id){
        if(id <= 0) throw new BadRequestException("CONTEST_ID_LESS_THAN_ZERO");
        return new ResponseEntity<>(service.gerContestProblems(id) , HttpStatus.OK);
    }


}
