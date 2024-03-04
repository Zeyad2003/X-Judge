package com.xjudge.controller.contest;

import com.xjudge.entity.Contest;
import com.xjudge.exception.XJudgeValidationException;
import com.xjudge.model.contest.modification.ContestClientRequest;
import com.xjudge.model.problem.ProblemModel;
import com.xjudge.model.submission.SubmissionInfoModel;
import com.xjudge.model.submission.SubmissionModel;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

    @PreAuthorize(value="@contestSecurity.authorizeCreateContest(principal.username , #creationModel.groupId , #creationModel.type)")
    @PostMapping
    public ResponseEntity<Contest> createContest(@Valid @RequestBody ContestClientRequest creationModel , BindingResult result , Authentication authentication) {
        if(result.hasErrors()){
            throw new XJudgeValidationException(result.getFieldErrors() ,XJudgeValidationException.VALIDATION_ERROR, ContestController.class.getName() , HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(contestService.createContest(creationModel , authentication), HttpStatus.OK);
    }

    @PreAuthorize(value = "@contestSecurity.authorizeGetContest(principal.username , #id , #password)")
    @GetMapping(value = "/{id}" , params = {"password"})
    public ResponseEntity<Contest> getContest(@PathVariable("id") Long id , @RequestParam("password") String password) {
        return new ResponseEntity<>(contestService.getContest(id), HttpStatus.OK);
    }

    @PreAuthorize(value = "@contestSecurity.authorizeGetContest(principal.username , #id , null)")
    @GetMapping("/{id}")
    public ResponseEntity<Contest> getGroupPContest(@PathVariable("id") Long id ) {
        return new ResponseEntity<>(contestService.getContest(id), HttpStatus.OK);
    }


    @PreAuthorize(value = "@contestSecurity.authorizeUpdateContest(principal.username , #model.groupId , #model.type, #id)")
    @PutMapping("/{id}")
    public ResponseEntity<Contest> updateContest(@PathVariable Long id
            , @Valid @RequestBody ContestClientRequest model, BindingResult result
        , Authentication authentication) {
        if(result.hasErrors()){
            throw new XJudgeValidationException(result.getFieldErrors(), XJudgeValidationException.VALIDATION_ERROR , ContestController.class.getName(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(contestService.updateContest(id, model , authentication), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContest(@PathVariable Long id) {
        contestService.deleteContest(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<SubmissionModel> submitContest(@PathVariable Long id, @Valid @RequestBody SubmissionInfoModel info , BindingResult result) {
        if(result.hasErrors()){
            throw new XJudgeValidationException(result.getFieldErrors(), XJudgeValidationException.VALIDATION_ERROR , ContestController.class.getName(), HttpStatus.BAD_REQUEST);
        }
        SubmissionModel submission = contestService.submitInContest(id, info);
        return new ResponseEntity<>(submission, HttpStatus.OK);
    }

    @GetMapping("/{id}/problems")
    public ResponseEntity<List<ProblemModel>> getContestProblems(@PathVariable Long id){
        return new ResponseEntity<>(contestService.getContestProblems(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/problem/{problemHashtag}")
    public ResponseEntity<ProblemModel> getContestProblem(@PathVariable Long id, @PathVariable String problemHashtag){
        return new ResponseEntity<>(contestService.getContestProblem(id, problemHashtag), HttpStatus.OK);
    }

    @GetMapping("/{id}/submissions")
    public ResponseEntity<List<SubmissionModel>> getContestSubmissions(@PathVariable Long id){
        return new ResponseEntity<>(contestService.getContestSubmissions(id), HttpStatus.OK);
    }

}
