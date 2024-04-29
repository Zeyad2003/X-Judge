package com.xjudge.controller.submission;

import com.xjudge.model.response.Response;
import com.xjudge.service.submission.SubmissionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/submission")
public class SubmissionController {
    private final SubmissionService submissionService;

    @GetMapping("/{id}")
    public ResponseEntity<Response> getSubmissionById(@PathVariable long id){
        Response response = Response.builder()
                .success(true)
                .data(submissionService.getSubmissionById(id))
                .message("Get dat of Submission " + id + "successfully")
                .build();
        return new ResponseEntity<>(response , HttpStatus.OK);
    }
}
