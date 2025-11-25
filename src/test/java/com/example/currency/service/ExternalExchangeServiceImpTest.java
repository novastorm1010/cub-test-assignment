package com.example.currency.service;

import com.example.currency.dto.CurrencyExchangeRateExternalDTO;
import com.example.currency.dto.CurrencyResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class ExternalExchangeServiceImpTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private ExternalExchangeServiceImp externalExchangeService;

    private static final String API_URL = "https://api.example.com/rates?base=%s&target=%s&start=%s&end=%s";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(externalExchangeService, "apiUrl", API_URL);
    }

    @Test
    void getCurrencyRates_Success() {
        // Arrange
        String baseCurrency = "EUR";
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);

        CurrencyExchangeRateExternalDTO rate1 = new CurrencyExchangeRateExternalDTO();
        CurrencyExchangeRateExternalDTO rate2 = new CurrencyExchangeRateExternalDTO();
        List<CurrencyExchangeRateExternalDTO> expectedRates = Arrays.asList(rate1, rate2);

        CurrencyResponse mockResponse = new CurrencyResponse();
        mockResponse.setResponse(expectedRates);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CurrencyResponse.class)).thenReturn(Mono.just(mockResponse));

        // Act
        List<CurrencyExchangeRateExternalDTO> result = externalExchangeService.getCurrencyRates(
                baseCurrency, startDate, endDate
        );

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedRates, result);

        verify(webClient, times(1)).get();
        verify(requestHeadersUriSpec, times(1)).uri(anyString());
        verify(requestHeadersSpec, times(1)).retrieve();
        verify(responseSpec, times(1)).bodyToMono(CurrencyResponse.class);
    }

    @Test
    void getCurrencyRates_VerifyCorrectUrl() {
        // Arrange
        String baseCurrency = "GBP";
        LocalDate startDate = LocalDate.of(2024, 2, 1);
        LocalDate endDate = LocalDate.of(2024, 2, 29);
        String expectedUrl = String.format(API_URL, baseCurrency, "USD", startDate, endDate);

        CurrencyResponse mockResponse = new CurrencyResponse();
        mockResponse.setResponse(Arrays.asList());

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(expectedUrl)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CurrencyResponse.class)).thenReturn(Mono.just(mockResponse));

        // Act
        externalExchangeService.getCurrencyRates(baseCurrency, startDate, endDate);

        // Assert
        verify(requestHeadersUriSpec, times(1)).uri(expectedUrl);
    }

    @Test
    void getCurrencyRates_EmptyResponse() {
        // Arrange
        String baseCurrency = "JPY";
        LocalDate startDate = LocalDate.of(2024, 3, 1);
        LocalDate endDate = LocalDate.of(2024, 3, 31);

        CurrencyResponse mockResponse = new CurrencyResponse();
        mockResponse.setResponse(Arrays.asList());

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CurrencyResponse.class)).thenReturn(Mono.just(mockResponse));

        // Act
        List<CurrencyExchangeRateExternalDTO> result = externalExchangeService.getCurrencyRates(
                baseCurrency, startDate, endDate
        );

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getCurrencyRates_NullResponse() {
        // Arrange
        String baseCurrency = "CHF";
        LocalDate startDate = LocalDate.of(2024, 4, 1);
        LocalDate endDate = LocalDate.of(2024, 4, 30);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CurrencyResponse.class)).thenReturn(Mono.empty());

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            externalExchangeService.getCurrencyRates(baseCurrency, startDate, endDate);
        });
    }

    @Test
    void getCurrencyRates_WebClientException() {
        // Arrange
        String baseCurrency = "AUD";
        LocalDate startDate = LocalDate.of(2024, 5, 1);
        LocalDate endDate = LocalDate.of(2024, 5, 31);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CurrencyResponse.class))
                .thenReturn(Mono.error(new RuntimeException("API Error")));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            externalExchangeService.getCurrencyRates(baseCurrency, startDate, endDate);
        });
    }

}