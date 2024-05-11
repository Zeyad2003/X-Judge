package com.xjudge.controller.onlinejudge;

import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.model.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OnlineJudgeController {

    @GetMapping("/online-judge")
    public ResponseEntity<?> getOnlineJudgeList() {
        Response response = Response.builder()
                .success(true)
                .data(OnlineJudgeType.values())
                .build();
        return ResponseEntity.ok(response);
    }

}
