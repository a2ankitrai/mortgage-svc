package com.ing.mortgage.service;

import com.ing.mortgage.dto.MortgageInterestRate;
import com.ing.mortgage.dto.MortgageCheckRequest;
import com.ing.mortgage.repository.MortgageRateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MortgageServiceTest {

    @Mock
    private MortgageRateRepository mortgageRateRepository;

    @InjectMocks
    private MortgageService mortgageService;


    @Test
    void shouldReturnNotFeasibleWhenNoRateFound() {
        when(mortgageRateRepository.findByMaturityPeriod(15)).thenReturn(Optional.empty()); // No rate for 15 years

        MortgageCheckRequest request = new MortgageCheckRequest(
                new BigDecimal("5000"),
                15,
                new BigDecimal("150000"),
                new BigDecimal("250000")
        );

        var response = mortgageService.checkMortgage(request);

        assertThat(response.feasible()).isFalse();
    }

    @Test
    void shouldReturnNotFeasibleWhenIncomeRuleFails() {
        var rate = new MortgageInterestRate(30, new BigDecimal("3.5"), Instant.now());
        when(mortgageRateRepository.findByMaturityPeriod(30)).thenReturn(Optional.of(rate));


        MortgageCheckRequest request = new MortgageCheckRequest(
                new BigDecimal("3000"), // income
                30,
                new BigDecimal("200000"), // loan value exceeds 4 * income
                new BigDecimal("300000")
        );


        var response = mortgageService.checkMortgage(request);
        assertThat(response.feasible()).isFalse();
    }

    @Test
    void shouldReturnNotFeasibleWhenHomeValueRuleFails() {
        var rate = new MortgageInterestRate(20, new BigDecimal("3.5"), Instant.now());
        when(mortgageRateRepository.findByMaturityPeriod(20)).thenReturn(Optional.of(rate));

        MortgageCheckRequest request = new MortgageCheckRequest(
                new BigDecimal("80000"), // income
                20,
                new BigDecimal("200000"), // loan value exceeds home value
                new BigDecimal("150000") // home value
        );
        var response = mortgageService.checkMortgage(request);
        assertThat(response.feasible()).isFalse();
    }

    @Test
    void shouldReturnFeasibleWhenRulesAreSatisfied() {

        var rate = new MortgageInterestRate(3, new BigDecimal("3.5"), Instant.now());
        when(mortgageRateRepository.findByMaturityPeriod(3)).thenReturn(Optional.of(rate));

        MortgageCheckRequest request = new MortgageCheckRequest(
                new BigDecimal("60000"),
                3,
                new BigDecimal("100000"),
                new BigDecimal("200000")
        );


        var response = mortgageService.checkMortgage(request);

        assertThat(response.feasible()).isTrue();
        assertThat(response.monthlyCost()).isPositive();
    }

    @Test
    void shouldCalculateMonthlyCostCorrectly() {

        var rate = new MortgageInterestRate(30, new BigDecimal("3.5"), Instant.now());
        when(mortgageRateRepository.findByMaturityPeriod(30)).thenReturn(Optional.of(rate));

        var request = new MortgageCheckRequest(
                new BigDecimal("100000"),
                30,
                new BigDecimal("200000"),
                new BigDecimal("250000")
        );

        var response = mortgageService.checkMortgage(request);

        // Monthly payment should be approximately $898 for this scenario
        assertThat(response.monthlyCost()).isBetween(
                new BigDecimal("890"),
                new BigDecimal("900")
        );
    }


}
