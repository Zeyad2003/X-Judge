package com.xjudge.controller.contest;

import com.xjudge.entity.Contest;
import com.xjudge.exception.XJudgeException;
import com.xjudge.model.contest.ContestCreationModel;
import com.xjudge.model.contest.ContestData;
import com.xjudge.model.enums.ContestType;
import com.xjudge.model.enums.ContestVisibility;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.xjudge.model.contest.ContestModel;
import com.xjudge.service.contest.ContestService;
import com.xjudge.model.problem.ContestProblemResp;

import com.github.dockerjava.api.exception.BadRequestException;

import java.time.Duration;
import java.time.Instant;
import java.time.Period;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contest")
public class ContestController {

    private final ContestService service;

    @PostMapping
    public ResponseEntity<Contest> createContest(@Valid @RequestBody ContestCreationModel creationModel) {
        return new ResponseEntity<>(service.createContest(creationModel), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contest> getContest(@PathVariable Long id) {
        return new ResponseEntity<>(service.getContest(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContest(@PathVariable Long id) {
        service.deleteContest(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

  /*  @GetMapping
    public ResponseEntity<List<ContestData>> getAllContests() {
        return new ResponseEntity<>(service.getAllContests(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContestData> getContest(@PathVariable Long id) {

        if (id <= 0) throw new BadRequestException("CONTEST_ID_LESS_THAN_ZERO");
        return new ResponseEntity<>(service.getContest(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContestModel> updateContest(@PathVariable Long id, @RequestBody ContestModel model) {

        if (id <= 0) throw new BadRequestException("CONTEST_ID_LESS_THAN_ZERO");
        return new ResponseEntity<>(service.updateContest(id, model), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContest(@PathVariable Long id) {

        if (id <= 0) throw new BadRequestException("CONTEST_ID_LESS_THAN_ZERO");
        service.deleteContest(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/problem")
    public ResponseEntity<List<ContestProblemResp>> getContestProblems(@PathVariable Long id) {
        if (id <= 0) throw new BadRequestException("CONTEST_ID_LESS_THAN_ZERO");
        return new ResponseEntity<>(service.gerContestProblems(id), HttpStatus.OK);
    }
*/
}
