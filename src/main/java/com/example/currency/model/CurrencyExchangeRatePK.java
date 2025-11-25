package com.example.currency.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrencyExchangeRatePK implements Serializable {

    @Column(name = "base_currency", length = 3)
    private String baseCurrency;

    @Column(name = "quote_currency", length = 3)
    private String quoteCurrency;

    @Column(name = "exchange_date")
    @Temporal(TemporalType.DATE)
    private LocalDate exchangeDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CurrencyExchangeRatePK)) return false;
        CurrencyExchangeRatePK that = (CurrencyExchangeRatePK) o;
        return Objects.equals(baseCurrency, that.baseCurrency) &&
                Objects.equals(quoteCurrency, that.quoteCurrency) &&
                Objects.equals(exchangeDate, that.exchangeDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseCurrency, quoteCurrency, exchangeDate);
    }

}
