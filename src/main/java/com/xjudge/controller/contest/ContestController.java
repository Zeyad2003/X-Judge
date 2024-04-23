package com.xjudge.controller.contest;

import com.xjudge.entity.Contest;
import com.xjudge.exception.XJudgeValidationException;
import com.xjudge.model.Pagination.PaginationResponse;
import com.xjudge.model.contest.ContestModel;
import com.xjudge.model.contest.ContestPageModel;
import com.xjudge.model.contest.modification.ContestClientRequest;
import com.xjudge.model.enums.ContestStatus;
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
    public  ResponseEntity<?> getAllContest(@RequestParam(defaultValue = "0") Integer pageNo,
                                                                  @RequestParam(defaultValue = "25") Integer size) {
        Pageable paging = PageRequest.of(pageNo, size);
        Page<ContestPageModel> contestPageModels = contestService.getAllContests(paging);
        PaginationResponse<ContestPageModel> pageResp = new PaginationResponse<>(contestPageModels.getTotalPages() , contestPageModels.getContent());
        Response response = Response.builder()
                .success(true)
                .data(pageResp)
                .build();
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @PreAuthorize(value="@contestSecurity.authorizeCreateContest(principal.username , #creationModel.groupId , #creationModel.type)")
    @PostMapping
    public ResponseEntity<Response> createContest(@Valid @RequestBody ContestClientRequest creationModel , BindingResult result , Authentication authentication) {
        if(result.hasErrors()){
            throw new XJudgeValidationException(result.getFieldErrors() ,XJudgeValidationException.VALIDATION_ERROR, ContestController.class.getName() , HttpStatus.BAD_REQUEST);
        }
        Response response = Response.builder()
                .success(true)
                .data(contestService.createContest(creationModel , authentication))
                .build();
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @PreAuthorize(value = "@contestSecurity.authorizeContestantsRoles(principal.username , #id , #password)")
    @GetMapping(value = "/{id}" )
    public ResponseEntity<Response> getContest(@PathVariable("id") Long id , @RequestParam(defaultValue = "") String password) {
        Response response = Response.builder()
                .success(true)
                .data(contestService.getContestData(id))
                .build();
        return new ResponseEntity<>(response , HttpStatus.OK);
    }


    @PreAuthorize(value = "@contestSecurity.authorizeUpdate(principal.username , #id ,#model.type ,#model.groupId)")
    @PutMapping("/{id}")
    public ResponseEntity<Response> updateContest(@PathVariable Long id
            , @Valid @RequestBody ContestClientRequest model, BindingResult result
        , Authentication authentication) {
        if(result.hasErrors()){
            throw new XJudgeValidationException(result.getFieldErrors(), XJudgeValidationException.VALIDATION_ERROR , ContestController.class.getName(), HttpStatus.BAD_REQUEST);
        }
        Response response = Response.builder()
                .success(true)
                .data(contestService.updateContest(id, model , authentication))
                .build();
        return new ResponseEntity<>(response , HttpStatus.OK);

    }

    @PreAuthorize(value = "@contestSecurity.authorizeDelete(principal.username , #id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContest(@PathVariable Long id) {
        contestService.deleteContest(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize(value = "@contestSecurity.authorizeContestantsRoles(principal.username , #id , #password)")
    @PostMapping("/{id}/submit")
    public ResponseEntity<Response> submitContest(@PathVariable Long id, @Valid @RequestBody SubmissionInfoModel info , BindingResult result , Authentication authentication ,  @RequestParam(defaultValue = "") String password) {
        if(result.hasErrors()){
            throw new XJudgeValidationException(result.getFieldErrors(), XJudgeValidationException.VALIDATION_ERROR , ContestController.class.getName(), HttpStatus.BAD_REQUEST);
        }
        SubmissionModel submission = contestService.submitInContest(id, info , authentication);
        Response response = Response.builder()
                .success(true)
                .data(submission)
                .build();
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @PreAuthorize(value = "@contestSecurity.authorizeContestantsRoles(principal.username , #id , #password)")
    @GetMapping("/{id}/problems")
    public ResponseEntity<Response> getContestProblems(@PathVariable Long id , @RequestParam(defaultValue = "") String password){
        Response response = Response.builder()
                .success(true)
                .data(contestService.getContestProblems(id))
                .build();
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @PreAuthorize(value = "@contestSecurity.authorizeContestantsRoles(principal.username , #id , #password)")
    @GetMapping("/{id}/problem/{problemHashtag}")
    public ResponseEntity<Response> getContestProblem(@PathVariable Long id, @PathVariable String problemHashtag ,  @RequestParam(defaultValue = "") String password){
        Response response = Response.builder()
                .success(true)
                .data(contestService.getContestProblem(id, problemHashtag))
                .build();
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @PreAuthorize(value = "@contestSecurity.authorizeContestantsRoles(principal.username , #id , #password)")
    @GetMapping("/{id}/submissions")
    public ResponseEntity<Response> getContestSubmissions(@PathVariable Long id ,  @RequestParam(defaultValue = "") String password){
        Response response = Response.builder()
                .success(true)
                .data(contestService.getContestSubmissions(id))
                .build();
        return new ResponseEntity<>(response , HttpStatus.OK);

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

    @GetMapping("/search")
    public  ResponseEntity<?> searchByTitle( @RequestParam(defaultValue = "" , required = false) String title ,
                                             @RequestParam(defaultValue = "" , required = false) String owner ,
                                             @RequestParam(defaultValue = "0") Integer pageNo,
                                             @RequestParam(defaultValue = "25") Integer size) {
        Pageable paging = PageRequest.of(pageNo, size);
        Page<ContestPageModel> contestPageModels = contestService.searchContestByTitleOrOwner(title ,owner , paging);
        PaginationResponse<ContestPageModel> pageResp = new PaginationResponse<>(contestPageModels.getTotalPages() , contestPageModels.getContent());
        Response response = Response.builder()
                .success(true)
                .data(pageResp)
                .build();
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @GetMapping(params = {"status"})
    public  ResponseEntity<?> searchByTitle( @RequestParam(defaultValue = "RUNNING") String status ,
                                             @RequestParam(defaultValue = "0") Integer pageNo,
                                             @RequestParam(defaultValue = "25") Integer size) {

        Page<ContestPageModel> contestPageModels = contestService.getContestByStatus(status, pageNo , size);
        PaginationResponse<ContestPageModel> pageResp = new PaginationResponse<>(contestPageModels.getTotalPages() , contestPageModels.getContent());
        Response response = Response.builder()
                .success(true)
                .data(pageResp)
                .build();
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

}
