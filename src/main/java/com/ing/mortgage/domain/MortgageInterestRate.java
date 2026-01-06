package com.ing.mortgage.domain;

import java.math.BigDecimal;
import java.time.Instant;

public record MortgageInterestRate(int maturityPeriod, BigDecimal interestRate, Instant lastUpdate) {
}