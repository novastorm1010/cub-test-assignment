package com.example.currency.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrencyExchangeRateExternalDTO {

    @JsonProperty("base_currency")
    private String baseCurrency;

    @JsonProperty("quote_currency")
    private String quoteCurrency;

    @JsonProperty("close_time")
    private Instant closeTime;

    @JsonProperty("average_bid")
    private BigDecimal averageBid;

    @JsonProperty("average_ask")
    private BigDecimal averageAsk;

    @JsonProperty("high_bid")
    private BigDecimal highBid;

    @JsonProperty("high_ask")
    private BigDecimal highAsk;

    @JsonProperty("low_bid")
    private BigDecimal lowBid;

    @JsonProperty("low_ask")
    private BigDecimal lowAsk;

}
