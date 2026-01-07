package com.ing.mortgage.it;

import com.ing.mortgage.dto.MortgageCheckRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MortgageControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllInterestRates() throws Exception {
        mockMvc.perform(get("/api/interest-rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(8)))
                .andExpect(jsonPath("$[0].maturityPeriod", notNullValue()))
                .andExpect(jsonPath("$[0].interestRate", notNullValue()));
    }

    @Test
    void shouldApproveFeasibleMortgage() throws Exception {
        var request = new MortgageCheckRequest(
                new BigDecimal("50000"),
                30,
                new BigDecimal("150000"),
                new BigDecimal("200000")
        );

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feasible", is(true)))
                .andExpect(jsonPath("$.monthlyCost", is(997.95)));
    }



    @Test
    void shouldRejectInfeasibleMortgage() throws Exception {
        var request = new MortgageCheckRequest(
                new BigDecimal("50000"),
                30,
                new BigDecimal("300000"),  // 6x income
                new BigDecimal("350000")
        );

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feasible", is(false)));
    }

    @Test
    void shouldReturnValidationErrorForInvalidRequest() throws Exception {
        var request = new MortgageCheckRequest(
                new BigDecimal("-1000"),  // invalid negative income
                30,
                new BigDecimal("150000"),
                new BigDecimal("200000")
        );

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Validation Failed")));
    }
}
