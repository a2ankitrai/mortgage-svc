package com.ing.mortgage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "Request parameters for mortgage feasibility check")
public record MortgageCheckRequest(
        @Schema(description = "Annual income", example = "50000", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Income is required")
        @DecimalMin(value = "0.01", message = "Income must be greater than zero") BigDecimal income,

        @Schema(description = "Loan maturity period",
                example = "30",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Maturity period is required")
        @Min(value = 1, message = "Maturity period must be at least 1 year")
        Integer maturityPeriod,

        @Schema(description = "Requested loan amount", example = "150000", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Loan value is required")
        @DecimalMin(value = "0.01", message = "Loan value must be greater than zero")
        BigDecimal loanValue,

        @Schema(description = "Value of the home", example = "200000", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Home value is required")
        @DecimalMin(value = "0.01", message = "Home value must be greater than zero")
        BigDecimal homeValue
) {
}
