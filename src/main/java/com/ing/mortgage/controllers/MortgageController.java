package com.ing.mortgage.controllers;

import com.ing.mortgage.dto.MortgageInterestRate;
import com.ing.mortgage.dto.MortgageCheckRequest;
import com.ing.mortgage.dto.MortgageCheckResponse;
import com.ing.mortgage.repository.MortgageRateRepository;
import com.ing.mortgage.service.MortgageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Mortgage API", description = "Endpoints for checking mortgage feasibility and interest rates")
public class MortgageController {

    private final MortgageService mortgageService;
    private final MortgageRateRepository mortgageRateRepository;


    public MortgageController(
            MortgageService mortgageService, MortgageRateRepository mortgageRateRepository) {
        this.mortgageRateRepository = mortgageRateRepository;
        this.mortgageService = mortgageService;
    }

    @GetMapping("/interest-rates")
    @Operation(
            summary = "Get all interest rates",
            description = "Retrieves the current interest rates for all available maturity periods"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved interest rates",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MortgageInterestRate.class)
                    )
            )
    })
    public List<MortgageInterestRate> getAllInterestRates() {
        return mortgageRateRepository.findAll();
    }

    @PostMapping("/mortgage-check")
    @Operation(
            summary = "Check mortgage feasibility",
            description = "Validates mortgage feasibility based on income and home value constraints, and calculates monthly costs."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful mortgage check",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MortgageCheckResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters"
            )
    })
    public MortgageCheckResponse checkMortgageFeasibility(@Valid @RequestBody MortgageCheckRequest request) {
        return mortgageService.checkMortgage(request);
    }
}
