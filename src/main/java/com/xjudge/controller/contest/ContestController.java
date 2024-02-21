package com.xjudge.controller.contest;

import com.xjudge.entity.Contest;
import com.xjudge.model.contest.ContestCreationModel;
import com.xjudge.model.contest.ContestProblemset;
import com.xjudge.model.contest.ContestUpdatingModel;
import com.xjudge.model.enums.ContestType;
import com.xjudge.model.enums.ContestVisibility;
import com.xjudge.model.enums.OnlineJudgeType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.xjudge.service.contest.ContestService;

import java.time.Instant;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contest")
public class ContestController {

    private final ContestService contestService;

    @GetMapping
    public ResponseEntity<List<Contest>> getAllProblems(@RequestParam(defaultValue = "0") Integer pageNo,
                                                        @RequestParam(defaultValue = "25") Integer size) {
        Pageable paging = PageRequest.of(pageNo, size);
        Page<Contest> pagedResult = contestService.getAllContests(paging);
        return new ResponseEntity<>(pagedResult.getContent(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Contest> createContest(@Valid @RequestBody ContestCreationModel creationModel) {
        return new ResponseEntity<>(contestService.createContest(creationModel), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contest> getContest(@PathVariable Long id) {
        return new ResponseEntity<>(contestService.getContest(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contest> updateContest(@PathVariable Long id, @Valid @RequestBody ContestUpdatingModel model) {
        return new ResponseEntity<>(contestService.updateContest(id, model), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteContest(@PathVariable Long id) {
        contestService.deleteContest(id);
        return new ResponseEntity<>("The Contest has been deleted successfully!!", HttpStatus.NO_CONTENT);
    }

  /*
    @PutMapping("/{id}")
    public ResponseEntity<ContestUserDataModel> updateContest(@PathVariable Long id, @RequestBody ContestUserDataModel model) {

        if (id <= 0) throw new BadRequestException("CONTEST_ID_LESS_THAN_ZERO");
        return new ResponseEntity<>(service.updateContest(id, model), HttpStatus.OK);

    }

    @GetMapping("/{id}/problem")
    public ResponseEntity<List<ContestProblemResp>> getContestProblems(@PathVariable Long id) {
        if (id <= 0) throw new BadRequestException("CONTEST_ID_LESS_THAN_ZERO");
        return new ResponseEntity<>(service.gerContestProblems(id), HttpStatus.OK);
    }
*/
}
