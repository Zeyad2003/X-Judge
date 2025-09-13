package com.xjudge.controller.problem;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.model.problem.ProblemDetails;
import com.xjudge.service.problem.ProblemFetchingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/** REST endpoints to retrieve problem details from supported online judges. */
@RestController
@RequiredArgsConstructor
@Tag(name = "Retrieve Problems", description = "Endpoints to fetch problem details from supported online judges")
@RequestMapping("problem")
public class ProblemFetchingController {
    private final ProblemFetchingService problemFetchingService;

    /**
     * Fetch a problem by its origin/platform and problem code.
     *
     * @param origin the online judge origin (e.g., CODEFORCES)
     * @param code the problem code/identifier on the origin site (e.g., 231A)
     * @return normalized problem details
     */
    @GetMapping(value = "/{origin}/{code}")
    @Operation(
            summary = "Fetch problem by origin and code",
            description =
                    "Returns normalized problem details by scraping/fetching from the specified online" + " judge.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Problem found",
                        content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                @ApiResponse(responseCode = "400", description = "Invalid origin or code", content = @Content),
                @ApiResponse(responseCode = "404", description = "Problem not found", content = @Content)
            })
    public ProblemDetails fetchByOriginAndCode(
            @Parameter(description = "Online judge origin (e.g., CODEFORCES)", required = true) @PathVariable("origin")
                    OnlineJudgeType origin,
            @Parameter(description = "Problem code/identifier on the origin site (e.g. 231A)", required = true)
                    @PathVariable("code")
                    String code) {
        return problemFetchingService.fetchByOriginAndCode(origin, code);
    }
}
