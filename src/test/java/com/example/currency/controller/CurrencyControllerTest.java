package com.example.currency.controller;

import com.example.currency.dto.CurrencyExchangeRateDTO;
import com.example.currency.dto.CurrencyExchangeRateExternalDTO;
import com.example.currency.service.CurrencyExchangeRateService;
import com.example.currency.service.ExternalExchangeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CurrencyController.class)
class CurrencyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CurrencyExchangeRateService currencyExchangeRateService;

    @MockBean
    private ExternalExchangeService externalExchangeService;

    @Test
    void testGetExternal_InvalidDate() throws Exception {
        mockMvc.perform(get("/api/v1/currencies/external")
                        .param("baseCurrency", "USD")
                        .param("startDate", "2024-10-10")
                        .param("endDate", "2024-10-10")
                        .locale(Locale.US))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    void testGetExternal_Valid() throws Exception {

        List<CurrencyExchangeRateExternalDTO> mockResult = List.of(
                CurrencyExchangeRateExternalDTO.builder()
                        .baseCurrency("VND")
                        .quoteCurrency("USD")
                        .closeTime(Instant.now())
                        .averageBid(BigDecimal.valueOf(0.0000390558))
                        .build()
        );

        Mockito.when(externalExchangeService.getCurrencyRates(anyString(), any(), any()))
                .thenReturn(mockResult);

        mockMvc.perform(get("/api/v1/currencies/external")
                        .param("baseCurrency", "VND")
                        .param("startDate", "2025-03-26")
                        .param("endDate", "2025-03-27"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].base_currency").value("VND"));
    }

    @Test
    void testGetInternal_InvalidDate() throws Exception {
        mockMvc.perform(get("/api/v1/currencies")
                        .param("baseCurrency", "USD")
                        .param("startDate", "2024-05-01")
                        .param("endDate", "2024-05-01"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    void testGetInternal_Valid() throws Exception {

        List<CurrencyExchangeRateDTO> mockList = List.of(
                CurrencyExchangeRateDTO.builder()
                        .exchangeDate(LocalDate.now())
                        .baseCurrency("VND")
                        .quoteCurrency("USD")
                        .closeTime(Instant.now())
                        .averageBid(BigDecimal.valueOf(0.0000390558))
                        .build()
        );

        Mockito.when(currencyExchangeRateService
                        .findCurrencyExchangeRate(anyString(), any(), any()))
                .thenReturn(mockList);

        mockMvc.perform(get("/api/v1/currencies")
                        .param("baseCurrency", "VND")
                        .param("startDate", "2024-01-01")
                        .param("endDate", "2024-01-05"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].baseCurrency").value("VND"));
    }

    @Test
    void testInsert() throws Exception {

        CurrencyExchangeRateDTO dto = CurrencyExchangeRateDTO.builder()
                                        .exchangeDate(LocalDate.now())
                                        .baseCurrency("VND")
                                        .quoteCurrency("USD")
                                        .closeTime(Instant.now())
                                        .averageBid(BigDecimal.valueOf(0.0000390558))
                                        .build();

        Mockito.when(currencyExchangeRateService.insertCurrency(any()))
                .thenReturn(dto);

        mockMvc.perform(post("/api/v1/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quoteCurrency").value("USD"));
    }

    @Test
    void testUpdate() throws Exception {

        CurrencyExchangeRateDTO dto = CurrencyExchangeRateDTO.builder()
                                        .exchangeDate(LocalDate.now())
                                        .baseCurrency("VND")
                                        .quoteCurrency("USD")
                                        .closeTime(Instant.now())
                                        .averageBid(BigDecimal.valueOf(0.0000390558))
                                        .build();

        Mockito.when(currencyExchangeRateService.updateCurrency(any()))
                .thenReturn(dto);

        mockMvc.perform(put("/api/v1/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.baseCurrency").value("VND"));
    }

    @Test
    void testDelete() throws Exception {

        mockMvc.perform(delete("/api/v1/currencies")
                        .param("exchangeDate", "2024-01-01")
                        .param("baseCurrency", "USD")
                        .param("quoteCurrency", "VND"))
                .andExpect(status().isNoContent());

        Mockito.verify(currencyExchangeRateService, Mockito.times(1))
                .deleteCurrency(any(), anyString(), anyString());
    }

}