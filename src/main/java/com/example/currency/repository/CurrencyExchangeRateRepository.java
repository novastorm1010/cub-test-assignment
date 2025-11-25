package com.example.currency.repository;

import com.example.currency.model.CurrencyExchangeRate;
import com.example.currency.model.CurrencyExchangeRatePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CurrencyExchangeRateRepository extends JpaRepository<CurrencyExchangeRate, CurrencyExchangeRatePK> {

    @Query("""
                SELECT c FROM CurrencyExchangeRate c
                WHERE c.primaryKey.baseCurrency = :baseCurrency
                AND c.primaryKey.quoteCurrency = 'USD'
                AND c.primaryKey.exchangeDate >= :startDate
                AND c.primaryKey.exchangeDate < :endDate
                ORDER BY c.closeTime
            """)
    List<CurrencyExchangeRate> findCurrenciesExchangeRate(@Param("baseCurrency") String baseCurrency
                                         , @Param("startDate") LocalDate startDate
                                         , @Param("endDate") LocalDate endDate);

}
