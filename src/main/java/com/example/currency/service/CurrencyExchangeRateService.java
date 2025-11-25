package com.example.currency.service;

import com.example.currency.dto.CurrencyExchangeRateDTO;
import com.example.currency.dto.CurrencyExchangeRateExternalDTO;

import java.time.LocalDate;
import java.util.List;

public interface CurrencyExchangeRateService {
    List<CurrencyExchangeRateDTO> findCurrencyExchangeRate(String baseCurrency,
                                                           LocalDate startDate,
                                                           LocalDate endDate);

    CurrencyExchangeRateDTO insertCurrency(CurrencyExchangeRateDTO dto);

    CurrencyExchangeRateDTO updateCurrency(CurrencyExchangeRateDTO dto);

    void deleteCurrency(LocalDate exchangeDate, String baseCurrency, String quoteCurrency);

    void saveAllCurrencies(List<CurrencyExchangeRateExternalDTO> currencyExchangeRateExternalDTOList);

}
