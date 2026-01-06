package com.ing.mortgage.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MortgageCheckRequest(
        @NotNull(message = "Income is required")
        @DecimalMin(value = "0.01", message = "Income must be greater than zero") BigDecimal income,

        @NotNull(message = "Maturity period is required")
        @Min(value = 1, message = "Maturity period must be at least 1 year")
        Integer maturityPeriod,

        @NotNull(message = "Loan value is required")
        @DecimalMin(value = "0.01", message = "Loan value must be greater than zero")
        BigDecimal loanValue,

        @NotNull(message = "Home value is required")
        @DecimalMin(value = "0.01", message = "Home value must be greater than zero")
        BigDecimal homeValue
) {
}
