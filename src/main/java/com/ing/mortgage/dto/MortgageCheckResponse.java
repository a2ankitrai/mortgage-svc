package com.ing.mortgage.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Mortgage feasibility check response with monthly cost calculation")
public record MortgageCheckResponse(
        @Schema(description = "Whether the mortgage request is feasible based on business rules", example = "true")
        Boolean feasible,

        @Schema(description = "Calculated monthly mortgage payment (principal + interest)", example = "1073.64")
        BigDecimal monthlyCost,

        @Schema(description = "List of validation messages or reasons for infeasibility")
        List<String> messages) {
}
