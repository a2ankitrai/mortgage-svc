package com.ing.mortgage.service;

import com.ing.mortgage.dto.MortgageCheckRequest;
import com.ing.mortgage.dto.MortgageCheckResponse;
import com.ing.mortgage.repository.MortgageRateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

@Service
public class MortgageService {

    private final MortgageRateRepository mortgageRateRepository;

    private static final Logger log = LoggerFactory.getLogger(MortgageService.class);

    // ideally this should come from configuration
    // hardcoded here for simplicity
    private static final BigDecimal MAX_LOAN_INCOME_MULTIPLIER = new BigDecimal("4");

    public MortgageService(MortgageRateRepository mortgageRateRepository) {
        this.mortgageRateRepository = mortgageRateRepository;
    }


    public MortgageCheckResponse checkMortgage(MortgageCheckRequest request) {
        log.debug("Checking mortgage feasibility for request: {}", request);

        var messages = new ArrayList<String>();
        var feasible = true;

        var maxLoanPossibleByIncome = request.income().multiply(MAX_LOAN_INCOME_MULTIPLIER);
        if (request.loanValue().compareTo(maxLoanPossibleByIncome) > 0) {
            feasible = false;
            messages.add("Requested loan exceeds maximum allowed based on income.");
        }

        var maxLoanPossibleByHomeValue = request.homeValue();
        if (request.loanValue().compareTo(maxLoanPossibleByHomeValue) > 0) {
            feasible = false;
            messages.add("Requested loan exceeds home value.");
        }

        var interestRateOpt = mortgageRateRepository.findByMaturityPeriod(request.maturityPeriod());
        BigDecimal monthlyCost = BigDecimal.ZERO;
        if (interestRateOpt.isEmpty()) {
            feasible = false;
            messages.add("No interest rate available for the requested maturity period.");
        } else {

            monthlyCost = calculateMonthlyCost(
                    request.loanValue(),
                    interestRateOpt.get().interestRate(),
                    request.maturityPeriod()
            );

            if (feasible) {
                messages.add("Mortgaged is feasible.");
            }
        }

        return new MortgageCheckResponse(feasible, monthlyCost, messages);
    }


    /**
     * monthly cost calculation using the formula:
     * M = P * [r(1+r)^n] / [(1+r)^n - 1]
     * Where:
     * M = monthly payment (principal and interest)
     * P = principal loan amount (the total amount borrowed)
     * r = the monthly interest rate ( annual interest rate / 12)
     * n = total number of payments ( loan term in years * 12)
     * <p>
     * source: https://en.wikipedia.org/wiki/Mortgage_calculator
     * code is copied and adapted.
     *
     */
    private BigDecimal calculateMonthlyCost(BigDecimal loanValue, BigDecimal annualInterestRate, int maturityPeriodYears) {

        var monthlyInterestRate = annualInterestRate
                .divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP)
                .divide(new BigDecimal(12), 10, RoundingMode.HALF_UP);
        var numberOfPayments = maturityPeriodYears * 12;

        if (monthlyInterestRate.compareTo(BigDecimal.ZERO) == 0) {
            return loanValue.divide(new BigDecimal(numberOfPayments), 2, RoundingMode.HALF_UP);
        }


        var onePlusRate = BigDecimal.ONE.add(monthlyInterestRate);
        var onePlusRatePowerN = onePlusRate.pow(numberOfPayments);
        var numerator = loanValue.multiply(monthlyInterestRate).multiply(onePlusRatePowerN);
        var denominator = onePlusRatePowerN.subtract(BigDecimal.ONE);

        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }
}
