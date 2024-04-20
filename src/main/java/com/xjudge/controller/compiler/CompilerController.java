package com.xjudge.controller.compiler;

import com.xjudge.entity.Compiler;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.model.response.Response;
import com.xjudge.service.compiler.CompilerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("compiler")
@Tag(name = "Compiler", description = "The Compiler End-Points for fetching compilers.")
public class CompilerController {
    private final CompilerService compilerService;

    @GetMapping
    @Operation(summary = "Get all compilers", description = "Get the available compilers in the system.")
    public ResponseEntity<?> getAllCompilers(@RequestParam String onlineJudge) {
        List<Compiler> compilers = compilerService.getCompilersByOnlineJudgeType(onlineJudge);

        Response response = Response.builder()
                .success(true)
                .data(compilers)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
