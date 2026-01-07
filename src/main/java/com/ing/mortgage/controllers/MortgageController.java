package com.ing.mortgage.controllers;

import com.ing.mortgage.domain.MortgageInterestRate;
import com.ing.mortgage.dto.MortgageCheckRequest;
import com.ing.mortgage.dto.MortgageCheckResponse;
import com.ing.mortgage.repository.MortgageRateRepository;
import com.ing.mortgage.service.MortgageService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MortgageController {

    private final MortgageService mortgageService;
    private final MortgageRateRepository mortgageRateRepository;


    public MortgageController(
            MortgageService mortgageService, MortgageRateRepository mortgageRateRepository) {
        this.mortgageRateRepository = mortgageRateRepository;
        this.mortgageService = mortgageService;
    }

    @GetMapping("/interest-rates")
    public List<MortgageInterestRate> getAllInterestRates() {
        return mortgageRateRepository.findAll();
    }

    @PostMapping("/mortgage-check")
    public MortgageCheckResponse checkMortgageFeasibility(@Valid @RequestBody MortgageCheckRequest request) {
        return mortgageService.checkMortgage(request);
    }
}
