package com.example.currency.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.currency.dto.CurrencyExchangeRateDTO;
import com.example.currency.dto.CurrencyExchangeRateExternalDTO;
import com.example.currency.error.ResourceAlreadyExistsException;
import com.example.currency.model.CurrencyExchangeRate;
import com.example.currency.model.CurrencyExchangeRatePK;
import com.example.currency.repository.CurrencyExchangeRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;

class CurrencyExchangeRateServiceImpTest {

    @Mock
    private CurrencyExchangeRateRepository currencyRateRepository;

    @InjectMocks
    private CurrencyExchangeRateServiceImp currencyExchangeRateService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindCurrencyExchangeRate() {

        CurrencyExchangeRatePK pk = new CurrencyExchangeRatePK();
        pk.setBaseCurrency("USD");
        pk.setQuoteCurrency("VND");
        pk.setExchangeDate(LocalDate.of(2025, 3, 26));


        CurrencyExchangeRate entity = CurrencyExchangeRate.builder()
                .primaryKey(pk)
                .closeTime(Instant.now())
                .averageBid(BigDecimal.valueOf(24500))
                .averageAsk(BigDecimal.valueOf(24550))
                .updateTime(LocalDateTime.now())
                .build();

        when(currencyRateRepository.findCurrenciesExchangeRate("USD", LocalDate.of(2025,3,26), LocalDate.of(2025,3,27)))
                .thenReturn(List.of(entity));

        List<CurrencyExchangeRateDTO> result = currencyExchangeRateService.findCurrencyExchangeRate(
                "USD",
                LocalDate.of(2025,3,26),
                LocalDate.of(2025,3,27)
        );

        assertEquals(1, result.size());
        assertEquals("USD", result.get(0).getBaseCurrency());
    }

    @Test
    void testInsertCurrency_Success() {
        CurrencyExchangeRateDTO dto = CurrencyExchangeRateDTO.builder()
                .baseCurrency("USD")
                .quoteCurrency("VND")
                .exchangeDate(LocalDate.of(2025,3,26))
                .closeTime(Instant.now())
                .averageBid(BigDecimal.valueOf(24500))
                .averageAsk(BigDecimal.valueOf(24550))
                .build();

        CurrencyExchangeRate entity = CurrencyExchangeRate.fromDTO(dto);

        when(currencyRateRepository.existsById(any(CurrencyExchangeRatePK.class))).thenReturn(false);
        when(currencyRateRepository.save(any(CurrencyExchangeRate.class))).thenReturn(entity);

        CurrencyExchangeRateDTO result = currencyExchangeRateService.insertCurrency(dto);

        assertEquals(dto.getBaseCurrency(), result.getBaseCurrency());
    }

    @Test
    void testInsertCurrency_AlreadyExists() {
        CurrencyExchangeRateDTO dto = CurrencyExchangeRateDTO.builder()
                .baseCurrency("USD")
                .quoteCurrency("VND")
                .exchangeDate(LocalDate.of(2025,3,26))
                .build();

        when(currencyRateRepository.existsById(any(CurrencyExchangeRatePK.class))).thenReturn(true);

        assertThrows(
                ResourceAlreadyExistsException.class,
                () -> currencyExchangeRateService.insertCurrency(dto)
        );
    }

    @Test
    void testUpdateCurrency_Success() {
        CurrencyExchangeRateDTO dto = CurrencyExchangeRateDTO.builder()
                .baseCurrency("USD")
                .quoteCurrency("VND")
                .exchangeDate(LocalDate.of(2025,3,26))
                .closeTime(Instant.now())
                .averageBid(BigDecimal.valueOf(24500))
                .averageAsk(BigDecimal.valueOf(24550))
                .build();

        CurrencyExchangeRate existing = CurrencyExchangeRate.fromDTO(dto);

        when(currencyRateRepository.findById(any(CurrencyExchangeRatePK.class))).thenReturn(Optional.of(existing));
        when(currencyRateRepository.save(any(CurrencyExchangeRate.class))).thenReturn(existing);

        CurrencyExchangeRateDTO result = currencyExchangeRateService.updateCurrency(dto);

        assertEquals(dto.getBaseCurrency(), result.getBaseCurrency());
    }

    @Test
    void testDeleteCurrency_Success() {
        CurrencyExchangeRatePK pk = new CurrencyExchangeRatePK("USD","VND",LocalDate.of(2025,3,26));
        CurrencyExchangeRate existing = new CurrencyExchangeRate();
        existing.setPrimaryKey(pk);

        when(currencyRateRepository.findById(pk)).thenReturn(Optional.of(existing));

        assertDoesNotThrow(() -> currencyExchangeRateService.deleteCurrency(LocalDate.of(2025,3,26),"USD","VND"));

        verify(currencyRateRepository, times(1)).delete(existing);
    }

    @Test
    void testSaveAllCurrencies() {
        CurrencyExchangeRateExternalDTO dto = CurrencyExchangeRateExternalDTO.builder()
                .baseCurrency("USD")
                .quoteCurrency("VND")
                .closeTime(Instant.now())
                .averageBid(BigDecimal.valueOf(24500))
                .averageAsk(BigDecimal.valueOf(24550))
                .build();

        when(currencyRateRepository.existsById(any(CurrencyExchangeRatePK.class))).thenReturn(false);

        currencyExchangeRateService.saveAllCurrencies(List.of(dto));

        verify(currencyRateRepository, times(1)).saveAll(anyList());
    }
}