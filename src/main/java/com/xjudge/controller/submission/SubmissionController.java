package com.xjudge.controller.submission;

import com.xjudge.model.response.Response;
import com.xjudge.service.submission.SubmissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/submission")
@Tag(name = "Submission", description = "The end-points related to submission operations.")
public class SubmissionController {
    private final SubmissionService submissionService;

    @GetMapping
    public ResponseEntity<Response> getAllSubmissions(@RequestParam(defaultValue = "0") Integer pageNo,
                                                      @RequestParam(defaultValue = "25") Integer size) {
        Pageable paging = PageRequest.of(pageNo, size);
        Response response = Response.builder()
                .success(true)
                .data(submissionService.getAllSubmissions(paging))
                .message("Get all Submissions successfully")
                .build();
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @GetMapping(params = {"userHandle" , "oj" , "problemCode" , "language"})
    public ResponseEntity<Response> filterSubmissions(@RequestParam(required = false ,defaultValue = "") String userHandle,
                                                      @RequestParam(required = false ,defaultValue = "") String oj,
                                                      @RequestParam(required = false ,defaultValue = "") String problemCode,
                                                      @RequestParam(required = false ,defaultValue = "") String language,
                                                      @RequestParam(defaultValue = "0") Integer pageNo,
                                                      @RequestParam(defaultValue = "25") Integer size) {
        Pageable paging = PageRequest.of(pageNo, size);
        Response response = Response.builder()
                .success(true)
                .data(submissionService.filterSubmissions(userHandle, oj, problemCode, language, paging))
                .message("Filtered Submissions successfully")
                .build();
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getSubmissionById(@PathVariable long id , Authentication authentication){
        Response response = Response.builder()
                .success(true)
                .data(submissionService.getSubmissionById(id , authentication))
                .message("Get data of Submission " + id + "successfully")
                .build();
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @PutMapping("/{id}/open")
    public ResponseEntity<Response> openSubmission(@PathVariable long id , Authentication authentication){
        Response response = Response.builder()
                .success(true)
                .data(submissionService.updateSubmissionOpen(id , authentication))
                .message("Update Submission " + id + "successfully")
                .build();
        return new ResponseEntity<>(response , HttpStatus.OK);
    }
}
