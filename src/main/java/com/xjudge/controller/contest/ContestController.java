package com.xjudge.controller.contest;

import com.xjudge.exception.XJudgeValidationException;
import com.xjudge.model.contest.ContestPageModel;
import com.xjudge.model.contest.ContestStatusPageModel;
import com.xjudge.model.contest.modification.ContestClientRequest;
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


@RestController
@RequiredArgsConstructor
@RequestMapping("/contest")
public class ContestController {

    private final ContestService contestService;

    @GetMapping
    public  ResponseEntity<?> getAllContest(@RequestParam(defaultValue = "0") Integer pageNo,
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
    @GetMapping(path = "/{id}/submissions" , params = {"userHandle" ,  "problemCode" , "result" , "language"})
    public ResponseEntity<Page<ContestStatusPageModel>> getContestSubmissions(@PathVariable Long id ,
                                                                              @RequestParam(required = false ,defaultValue = "") String userHandle,
                                                                              @RequestParam(required = false ,defaultValue = "") String problemCode,
                                                                              @RequestParam(required = false ,defaultValue = "") String result,
                                                                              @RequestParam(required = false ,defaultValue = "") String language,
                                                                              @RequestParam(defaultValue = "0") Integer pageNo,
                                                                              @RequestParam(defaultValue = "25") Integer size ,
                                                                              @RequestParam(defaultValue = "") String password){
        Pageable pageable = PageRequest.of(pageNo, size);
        return new ResponseEntity<>(contestService.getContestSubmissions(id,userHandle , problemCode,result , language , pageable) , HttpStatus.OK);
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
    public  ResponseEntity<?> searchByTitleAndOwner( @RequestParam(defaultValue = "" , required = false) String title ,
                                             @RequestParam(defaultValue = "" , required = false) String owner ,
                                             @RequestParam(defaultValue = "0") Integer pageNo,
                                             @RequestParam(defaultValue = "25") Integer size) {
        Pageable paging = PageRequest.of(pageNo, size);
        Page<ContestPageModel> contestPageModels = contestService.searchContestByTitleOrOwner(title ,owner , paging);
        Response response = Response.builder()
                .success(true)
                .data(contestPageModels)
                .build();
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @GetMapping(params = {"status"})
    public  ResponseEntity<?> filterByStatus( @RequestParam(defaultValue = "RUNNING") String status ,
                                             @RequestParam(defaultValue = "0") Integer pageNo,
                                             @RequestParam(defaultValue = "25") Integer size) {
        Response response = Response.builder()
                .success(true)
                .data(contestService.getContestByStatus(status, pageNo , size))
                .build();
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @GetMapping(params = {"type" , "status"})
    public ResponseEntity<?> getContestByType(@RequestParam(defaultValue = "CLASSIC") String type , @RequestParam(required = false , defaultValue = "") String status ,
                                               @RequestParam(defaultValue = "0") Integer pageNo,
                                               @RequestParam(defaultValue = "25") Integer size){
        Pageable pageable = PageRequest.of(pageNo , size);
        Response response = Response.builder()
                .success(true)
                .data(contestService.getContestsByType(type , status , pageable))
                .build();
        return new ResponseEntity<>(response , HttpStatus.OK);
    }


    @GetMapping(params = {"visibility" , "status"})
    public ResponseEntity<?> getContestByVisibility(@RequestParam(defaultValue = "PUBLIC") String visibility , @RequestParam(required = false , defaultValue = "") String status ,
                                              @RequestParam(defaultValue = "0") Integer pageNo,
                                              @RequestParam(defaultValue = "25") Integer size){
        Pageable pageable = PageRequest.of(pageNo , size);
        Response response = Response.builder()
                .success(true)
                .data(contestService.getContestsByVisibility(visibility , status , pageable))
                .build();
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @GetMapping("/mine")
    public ResponseEntity<?> getContestOfUser(Authentication authentication , @RequestParam(required = false , defaultValue = "") String status ,
                                                    @RequestParam(defaultValue = "0") Integer pageNo,
                                                    @RequestParam(defaultValue = "25") Integer size){
        Pageable pageable = PageRequest.of(pageNo , size);
        Response response = Response.builder()
                .success(true)
                .data(contestService.getContestsOfLoginUser(authentication , status , pageable))
                .build();
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @GetMapping(params = {"category" , "status" , "title" , "owner"})
    public ResponseEntity<?> globalSearch(@RequestParam(defaultValue = "") String category, @RequestParam(required = false , defaultValue = "") String status ,
                                                    @RequestParam(defaultValue = "" , required = false) String title ,
                                                    @RequestParam(defaultValue = "" , required = false) String owner ,
                                                    @RequestParam(defaultValue = "0") Integer pageNo,
                                                    @RequestParam(defaultValue = "25") Integer size , Authentication authentication){
        if(category.equals("mine")) category = authentication.getName();
        Pageable pageable = PageRequest.of(pageNo , size);
        Response response = Response.builder()
                .success(true)
                .data(contestService.searchByVisibilityOrTypeOrUserAndOwnerAndTitle(category , title , owner ,status, pageable))
                .build();
        return new ResponseEntity<>(response , HttpStatus.OK);
    }
}
