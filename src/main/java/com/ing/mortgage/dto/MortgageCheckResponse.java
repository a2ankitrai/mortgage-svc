package com.ing.mortgage.dto;

import java.math.BigDecimal;
import java.util.List;

public record MortgageCheckResponse(boolean feasible, BigDecimal monthlyCost, List<String> messages) {
}
