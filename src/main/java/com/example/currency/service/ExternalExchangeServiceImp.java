package com.example.currency.service;

import com.example.currency.dto.CurrencyExchangeRateExternalDTO;
import com.example.currency.dto.CurrencyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExternalExchangeServiceImp implements ExternalExchangeService {

    private final WebClient webClient;

    @Value("${currency-external.endpoint.currencies}")
    private String apiUrl;

    @Override
    public List<CurrencyExchangeRateExternalDTO> getCurrencyRates(String baseCurrency, LocalDate startDate, LocalDate endDate) {
        String url = String.format(apiUrl, baseCurrency, "USD", startDate, endDate);

        CurrencyResponse response = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(CurrencyResponse.class)
                .block();


        return response.getResponse();
    }

}
