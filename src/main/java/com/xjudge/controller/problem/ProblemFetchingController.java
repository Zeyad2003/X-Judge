package com.xjudge.controller.problem;

import com.xjudge.model.enums.FetchingStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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

/**
 * REST endpoints to retrieve/scrap problem details from supported online judges.
 */
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
     * @param code   the problem code/identifier on the origin site (e.g., 231A)
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
    public ResponseEntity<ProblemDetails> fetchByOriginAndCode(
            @Parameter(description = "Online judge origin (e.g., CODEFORCES)", required = true) @PathVariable("origin")
            OnlineJudgeType origin,
            @Parameter(description = "Problem code/identifier on the origin site (e.g. 231A)", required = true)
            @PathVariable("code")
            String code) {

        ProblemDetails problemDetails = problemFetchingService.fetchByOriginAndCode(origin, code);
        return new ResponseEntity<>(problemDetails, HttpStatus.ACCEPTED);
    }

    /**
     * Initiates a background job to fetch and update a problem by its origin/platform and code.
     * This is an idempotent operation that either creates or updates the problem details.
     *
     * @param origin the online judge origin (e.g., CODEFORCES)
     * @param code   the problem code/identifier on the origin site (e.g., 231A)
     * @return ResponseEntity with status 202 if the request is accepted for processing
     */
    @PutMapping(value = "/{origin}/{code}")
    @Operation(
            summary = "Fetch or update a problem",
            description =
                    "Initiates a background job to scrape and save (or update) a problem from an online judge. " +
                            "This is an idempotent operation.",
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "Request accepted for processing."),
                    @ApiResponse(responseCode = "400", description = "Invalid origin or code", content = @Content)
            })
    public ResponseEntity<String> fetchOrUpdateProblem(
            @Parameter(description = "Online judge origin (e.g., CODEFORCES)", required = true) @PathVariable("origin")
            OnlineJudgeType origin,
            @Parameter(description = "Problem code/identifier on the origin site (e.g. 231A)", required = true)
            @PathVariable("code")
            String code) {

        problemFetchingService.triggerFetchOrUpdate(origin, code);
        return new ResponseEntity<>("Request to fetch/update problem from platform" + origin + "with code: " + code + " has been accepted.\n" +
                "Wait a couple of seconds and check its status at the /problem/status/{origin}/{code} endpoint.", HttpStatus.ACCEPTED);
    }

    /**
     * Returns the fetching status of a problem for a given online judge and code.
     * Used by clients to check if the problem has been fetched or is still being processed.
     *
     * @param ojType the online judge type (e.g., CODEFORCES)
     * @param code   the problem code/identifier on the origin site
     * @return ResponseEntity containing the current FetchingStatus
     */
    @GetMapping(value = "/status/{ojType}/{code}")
    @Operation(
            summary = "Check problem fetching status",
            description = "Returns the current fetching status of a problem for a given online judge and code.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Fetching status returned",
                            content = @Content(schema = @Schema(implementation = FetchingStatus.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid online judge type or code", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Problem not found", content = @Content)
            })
    public ResponseEntity<FetchingStatus> checkProblemFetchingStatus(
            @Parameter(description = "Online judge type (e.g., CODEFORCES)", required = true) @PathVariable("ojType") OnlineJudgeType ojType,
            @Parameter(description = "Problem code/identifier on the origin site", required = true) @PathVariable("code") String code) {
        return new ResponseEntity<>(problemFetchingService.getProblemFetchingStatus(ojType, code), HttpStatus.OK);
    }
}
