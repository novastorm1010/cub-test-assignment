package com.example.currency.service;

import com.example.currency.dto.CurrencyExchangeRateExternalDTO;

import java.time.LocalDate;
import java.util.List;

public interface ExternalExchangeService {
    List<CurrencyExchangeRateExternalDTO> getCurrencyRates(String baseCurrency, LocalDate startDate, LocalDate endDate);
}
