package com.xjudge.controller.contest;

import com.xjudge.exception.XJudgeValidationException;
import com.xjudge.model.contest.ContestModel;
import com.xjudge.model.contest.ContestPageModel;
import com.xjudge.model.contest.modification.ContestClientRequest;
import com.xjudge.model.problem.ProblemModel;
import com.xjudge.model.response.Response;
import com.xjudge.model.submission.SubmissionInfoModel;
import com.xjudge.model.submission.SubmissionModel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public  ResponseEntity<?> getAllProblems(@RequestParam(defaultValue = "0") Integer pageNo,
                                                                  @RequestParam(defaultValue = "25") Integer size) {
        Pageable paging = PageRequest.of(pageNo, size);
        Page<ContestPageModel> contestPagesData = contestService.getAllContests(paging);

        Response response = Response.builder()
                .success(true)
                .data(contestPagesData)
                .build();
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @PreAuthorize(value="@contestSecurity.authorizeCreateContest(principal.username , #creationModel.groupId , #creationModel.type)")
    @PostMapping
    public ResponseEntity<ContestModel> createContest(@Valid @RequestBody ContestClientRequest creationModel , BindingResult result , Authentication authentication) {
        if(result.hasErrors()){
            throw new XJudgeValidationException(result.getFieldErrors() ,XJudgeValidationException.VALIDATION_ERROR, ContestController.class.getName() , HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(contestService.createContest(creationModel , authentication), HttpStatus.OK);
    }

    @PreAuthorize(value = "@contestSecurity.authorizeContestantsRoles(principal.username , #id , #password)")
    @GetMapping(value = "/{id}" )
    public ResponseEntity<ContestModel> getContest(@PathVariable("id") Long id , @RequestParam(defaultValue = "") String password) {
        return new ResponseEntity<>(contestService.getContestData(id), HttpStatus.OK);
    }


    @PreAuthorize(value = "@contestSecurity.authorizeUpdate(principal.username , #id ,#model.type ,#model.groupId)")
    @PutMapping("/{id}")
    public ResponseEntity<ContestModel> updateContest(@PathVariable Long id
            , @Valid @RequestBody ContestClientRequest model, BindingResult result
        , Authentication authentication) {
        if(result.hasErrors()){
            throw new XJudgeValidationException(result.getFieldErrors(), XJudgeValidationException.VALIDATION_ERROR , ContestController.class.getName(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(contestService.updateContest(id, model , authentication), HttpStatus.OK);
    }

    @PreAuthorize(value = "@contestSecurity.authorizeDelete(principal.username , #id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContest(@PathVariable Long id) {
        contestService.deleteContest(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize(value = "@contestSecurity.authorizeContestantsRoles(principal.username , #id , #password)")
    @PostMapping("/{id}/submit")
    public ResponseEntity<SubmissionModel> submitContest(@PathVariable Long id, @Valid @RequestBody SubmissionInfoModel info , BindingResult result , Authentication authentication ,  @RequestParam(defaultValue = "") String password) {
        if(result.hasErrors()){
            throw new XJudgeValidationException(result.getFieldErrors(), XJudgeValidationException.VALIDATION_ERROR , ContestController.class.getName(), HttpStatus.BAD_REQUEST);
        }
        SubmissionModel submission = contestService.submitInContest(id, info , authentication);
        return new ResponseEntity<>(submission, HttpStatus.OK);
    }

    @PreAuthorize(value = "@contestSecurity.authorizeContestantsRoles(principal.username , #id , #password)")
    @GetMapping("/{id}/problems")
    public ResponseEntity<List<ProblemModel>> getContestProblems(@PathVariable Long id , @RequestParam(defaultValue = "") String password){
        return new ResponseEntity<>(contestService.getContestProblems(id), HttpStatus.OK);
    }

    @PreAuthorize(value = "@contestSecurity.authorizeContestantsRoles(principal.username , #id , #password)")
    @GetMapping("/{id}/problem/{problemHashtag}")
    public ResponseEntity<ProblemModel> getContestProblem(@PathVariable Long id, @PathVariable String problemHashtag ,  @RequestParam(defaultValue = "") String password){
        return new ResponseEntity<>(contestService.getContestProblem(id, problemHashtag), HttpStatus.OK);
    }

    @PreAuthorize(value = "@contestSecurity.authorizeContestantsRoles(principal.username , #id , #password)")
    @GetMapping("/{id}/submissions")
    public ResponseEntity<List<SubmissionModel>> getContestSubmissions(@PathVariable Long id ,  @RequestParam(defaultValue = "") String password){
        return new ResponseEntity<>(contestService.getContestSubmissions(id), HttpStatus.OK);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/{id}/rank")
    @PreAuthorize(value = "@contestSecurity.authorizeContestantsRoles(principal.username , #id , #password)")
    public ResponseEntity<Response> getRank(@PathVariable long id , @RequestParam(defaultValue = "") String password){
       Response response =  Response.builder()
               .data(contestService.getRank(id))
               .success(true)
               .build();
       return new ResponseEntity<>(response , HttpStatus.OK);
    }

}
