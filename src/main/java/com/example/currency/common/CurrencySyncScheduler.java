package com.example.currency.common;

import com.example.currency.dto.CurrencyExchangeRateExternalDTO;
import com.example.currency.model.CurrencyExchangeRate;
import com.example.currency.repository.CurrencyExchangeRateRepository;
import com.example.currency.service.CurrencyExchangeRateService;
import com.example.currency.service.ExternalExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurrencySyncScheduler {
    private final ExternalExchangeService externalExchangeService;
    private final CurrencyExchangeRateService currencyExchangeRateService;

    private static final List<String> BASE_CURRENCIES = List.of("VND", "EUR", "TWD");


    @Scheduled(cron = "0 */2 * * * *")
    public void syncCurrencyRates() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        for (String baseCurrency : BASE_CURRENCIES) {
            try {
                log.info("Start syncing currency rates for {} -> USD, date range: {} - {}",
                        baseCurrency, yesterday, today);

                List<CurrencyExchangeRateExternalDTO> externalRates =
                        externalExchangeService.getCurrencyRates(baseCurrency, yesterday, today);

                currencyExchangeRateService.saveAllCurrencies(externalRates);

                log.info("Successfully synced {} -> USD for {} to {}",
                        baseCurrency, yesterday, today);

            } catch (Exception e) {
                log.error("Error syncing {} -> USD: {}", baseCurrency, e.getMessage(), e);
            }
        }
    }
}
