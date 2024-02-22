package com.xjudge.controller.contest;

import com.xjudge.entity.Contest;
import com.xjudge.entity.Submission;
import com.xjudge.exception.XJudgeValidationException;
import com.xjudge.model.contest.ContestCreationModel;
import com.xjudge.model.contest.ContestUpdatingModel;
import com.xjudge.model.problem.ProblemModel;
import com.xjudge.model.submission.SubmissionInfoModel;
import com.xjudge.model.submission.SubmissionModel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.xjudge.service.contest.ContestService;

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
    public ResponseEntity<Contest> createContest(@Valid @RequestBody ContestCreationModel creationModel , BindingResult result) {
        if(result.hasErrors()){
            throw new XJudgeValidationException(result.getFieldErrors() ,XJudgeValidationException.VALIDATION_ERROR, ContestController.class.getName() , HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(contestService.createContest(creationModel), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contest> getContest(@PathVariable Long id) {
        return new ResponseEntity<>(contestService.getContest(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contest> updateContest(@PathVariable Long id
            , @Valid @RequestBody ContestUpdatingModel model, BindingResult result) {
        if(result.hasErrors()){
            throw new XJudgeValidationException(result.getFieldErrors(), XJudgeValidationException.VALIDATION_ERROR , ContestController.class.getName(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(contestService.updateContest(id, model), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteContest(@PathVariable Long id) {
        contestService.deleteContest(id);
        return new ResponseEntity<>("The Contest has been deleted successfully!!", HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<SubmissionModel> submitContest(@PathVariable Long id, @Valid @RequestBody SubmissionInfoModel info) {
        SubmissionModel submission = contestService.submitInContest(id, info);
        return new ResponseEntity<>(submission, HttpStatus.OK);
    }

    @GetMapping("/{id}/problems")
    public ResponseEntity<List<ProblemModel>> getContestProblems(@PathVariable Long id){
        return new ResponseEntity<>(contestService.getContestProblems(id), HttpStatus.OK);
    }

}
