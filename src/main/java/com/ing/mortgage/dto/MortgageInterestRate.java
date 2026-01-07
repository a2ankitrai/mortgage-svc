package com.ing.mortgage.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;

@Schema(description = "Interest rate information for a specific maturity period")
public record MortgageInterestRate(
        @Schema(description = "Maturity period in years", example = "30")
        Integer maturityPeriod,

        @Schema(description = "Annual interest rate as a percentage", example = "7.00")
        BigDecimal interestRate,

        @Schema(description = "Timestamp when this rate was last updated")
        Instant lastUpdate) {
}