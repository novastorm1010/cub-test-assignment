package com.example.currency.model;

import com.example.currency.dto.CurrencyExchangeRateDTO;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "currencies_exchange_rate")
public class CurrencyExchangeRate {

    @EmbeddedId
    private CurrencyExchangeRatePK primaryKey;

    @Column(name = "close_time", nullable = false)
    private Instant closeTime;

    @Column(name = "average_bid", precision = 20, scale = 10)
    private BigDecimal averageBid;

    @Column(name = "average_ask", precision = 20, scale = 10)
    private BigDecimal averageAsk;

    @Column(name = "high_bid", precision = 20, scale = 10)
    private BigDecimal highBid;

    @Column(name = "high_ask", precision = 20, scale = 10)
    private BigDecimal highAsk;

    @Column(name = "low_bid", precision = 20, scale = 10)
    private BigDecimal lowBid;

    @Column(name = "low_ask", precision = 20, scale = 10)
    private BigDecimal lowAsk;

    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    public static CurrencyExchangeRate fromDTO(CurrencyExchangeRateDTO currencyDTO){
        return CurrencyExchangeRate.builder()
                .primaryKey(new CurrencyExchangeRatePK(
                        currencyDTO.getBaseCurrency(),
                        currencyDTO.getQuoteCurrency(),
                        currencyDTO.getExchangeDate()
                ))
                .closeTime(currencyDTO.getCloseTime())
                .averageBid(currencyDTO.getAverageBid())
                .averageAsk(currencyDTO.getAverageAsk())
                .highBid(currencyDTO.getHighBid())
                .highAsk(currencyDTO.getHighAsk())
                .lowBid(currencyDTO.getLowBid())
                .lowAsk(currencyDTO.getLowAsk())
                .updateTime(LocalDateTime.now())
                .build();
    }

    public CurrencyExchangeRateDTO toDomain (){
        return CurrencyExchangeRateDTO.builder()
                .exchangeDate(primaryKey.getExchangeDate())
                .baseCurrency(primaryKey.getBaseCurrency())
                .quoteCurrency(primaryKey.getQuoteCurrency())
                .closeTime(closeTime)
                .averageBid(averageBid)
                .averageAsk(averageAsk)
                .highBid(highBid)
                .highAsk(highAsk)
                .lowBid(lowBid)
                .lowAsk(lowAsk)
                .updateTime(updateTime)
                .build();
    }

}
