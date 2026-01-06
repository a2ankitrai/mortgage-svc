package com.ing.mortgage.repository;

import com.ing.mortgage.domain.MortgageInterestRate;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Repository
public class MortgageRateRepository {

    private static final Logger logger = LoggerFactory.getLogger(MortgageRateRepository.class);
    private final Map<Integer, MortgageInterestRate> rates = new HashMap<>();

    // initializing sample interest rates for different maturity periods.
    // In real service this should come from a database or another store.
    @PostConstruct
    public void initializeInterestRates() {

        var now = Instant.now();
        List.of(
                new MortgageInterestRate(1, new BigDecimal("4.50"), now),
                new MortgageInterestRate(3, new BigDecimal("4.85"), now),
                new MortgageInterestRate(5, new BigDecimal("5.25"), now),
                new MortgageInterestRate(10, new BigDecimal("5.75"), now),
                new MortgageInterestRate(15, new BigDecimal("6.15"), now),
                new MortgageInterestRate(20, new BigDecimal("6.50"), now),
                new MortgageInterestRate(25, new BigDecimal("6.75"), now),
                new MortgageInterestRate(30, new BigDecimal("7.00"), now)
        ).forEach(rate -> rates.put(rate.maturityPeriod(), rate));

        logger.info("Initialized mortgage interest rates: {}", rates.size());
    }

    public List<MortgageInterestRate> findAll() {
        return rates.values()
                .stream()
                .sorted(Comparator.comparing(MortgageInterestRate::maturityPeriod))
                .toList();
    }

    public Optional<MortgageInterestRate> findByMaturityPeriod(Integer maturityPeriod) {
        return Optional.ofNullable(rates.get(maturityPeriod));
    }
}
