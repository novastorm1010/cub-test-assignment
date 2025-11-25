package com.example.currency.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrencyExchangeRateDTO {
    private LocalDate exchangeDate;
    private String baseCurrency;
    private String quoteCurrency;
    private Instant closeTime;
    private BigDecimal averageBid;
    private BigDecimal averageAsk;
    private BigDecimal highBid;
    private BigDecimal highAsk;
    private BigDecimal lowBid;
    private BigDecimal lowAsk;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
