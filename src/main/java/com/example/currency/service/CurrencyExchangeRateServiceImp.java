package com.example.currency.service;

import com.example.currency.dto.CurrencyExchangeRateDTO;
import com.example.currency.dto.CurrencyExchangeRateExternalDTO;
import com.example.currency.error.ResourceAlreadyExistsException;
import com.example.currency.error.ResourceNotFoundException;
import com.example.currency.mapper.CurrencyExchangeRateMapper;
import com.example.currency.model.CurrencyExchangeRate;
import com.example.currency.model.CurrencyExchangeRatePK;
import com.example.currency.repository.CurrencyExchangeRateRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CurrencyExchangeRateServiceImp implements  CurrencyExchangeRateService {

    private final CurrencyExchangeRateRepository currencyRateRepository;
    private final CurrencyExchangeRateMapper currencyExchangeRateMapper;

    @Override
    public List<CurrencyExchangeRateDTO> findCurrencyExchangeRate(String baseCurrency
                                                                , LocalDate startDate
                                                                , LocalDate endDate){

        List<CurrencyExchangeRate> currencyExchangeRateList = currencyRateRepository
                                                                .findCurrenciesExchangeRate(baseCurrency, startDate, endDate);

        return currencyExchangeRateList.stream()
                .map(CurrencyExchangeRate::toDomain).toList();
    }

    @Override
    @Transactional
    public CurrencyExchangeRateDTO insertCurrency(CurrencyExchangeRateDTO dto) {
        CurrencyExchangeRatePK pk = new CurrencyExchangeRatePK(dto.getBaseCurrency(),
                dto.getQuoteCurrency(),
                dto.getExchangeDate());


        if (currencyRateRepository.existsById(pk)) {
            throw new ResourceAlreadyExistsException(
                    String.format("CurrencyExchangeRate already exists for %s-%s on %s",
                            dto.getBaseCurrency(),
                            dto.getQuoteCurrency(),
                            dto.getExchangeDate())
            );
        }

        CurrencyExchangeRate entity = CurrencyExchangeRate.fromDTO(dto);
        return currencyRateRepository.save(entity).toDomain();
    }

    @Override
    @Transactional
    public CurrencyExchangeRateDTO updateCurrency(CurrencyExchangeRateDTO dto) {
        CurrencyExchangeRatePK pk = new CurrencyExchangeRatePK(dto.getBaseCurrency(),
                dto.getQuoteCurrency(),
                dto.getExchangeDate());


        CurrencyExchangeRate existingRecord = currencyRateRepository.findById(pk)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("CurrencyExchangeRate not found for %s-%s on %s",
                                dto.getBaseCurrency(),
                                dto.getQuoteCurrency(),
                                dto.getExchangeDate())
                ));

        existingRecord.setCloseTime(dto.getCloseTime());
        existingRecord.setAverageBid(dto.getAverageBid());
        existingRecord.setAverageAsk(dto.getAverageAsk());
        existingRecord.setHighBid(dto.getHighBid());
        existingRecord.setHighAsk(dto.getHighAsk());
        existingRecord.setLowBid(dto.getLowBid());
        existingRecord.setLowAsk(dto.getLowAsk());
        existingRecord.setUpdateTime(LocalDateTime.now());

        return currencyRateRepository.save(existingRecord).toDomain();
    }

    @Override
    @Transactional
    public void deleteCurrency(LocalDate exchangeDate, String baseCurrency, String quoteCurrency) {
        CurrencyExchangeRatePK pk = new CurrencyExchangeRatePK(baseCurrency, quoteCurrency, exchangeDate);

        CurrencyExchangeRate existingRecord = currencyRateRepository.findById(pk)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("CurrencyExchangeRate not found for %s-%s on %s",
                                exchangeDate,
                                baseCurrency,
                                quoteCurrency)
                ));

        currencyRateRepository.delete(existingRecord);
    }

    @Override
    public void saveAllCurrencies(List<CurrencyExchangeRateExternalDTO> currencyExchangeRateExternalDTOList) {
        List<CurrencyExchangeRate> entities = currencyExchangeRateExternalDTOList.stream()
                .map(dto -> {
                    LocalDate exchangeDate = dto.getCloseTime().atZone(ZoneOffset.UTC).toLocalDate();

                    CurrencyExchangeRatePK pk = new CurrencyExchangeRatePK();
                    pk.setBaseCurrency(dto.getBaseCurrency());
                    pk.setQuoteCurrency(dto.getQuoteCurrency());
                    pk.setExchangeDate(exchangeDate);

                    CurrencyExchangeRate entity = new CurrencyExchangeRate();
                    entity.setPrimaryKey(pk);
                    entity.setCloseTime(dto.getCloseTime());
                    entity.setAverageBid(dto.getAverageBid());
                    entity.setAverageAsk(dto.getAverageAsk());
                    entity.setHighBid(dto.getHighBid());
                    entity.setHighAsk(dto.getHighAsk());
                    entity.setLowBid(dto.getLowBid());
                    entity.setLowAsk(dto.getLowAsk());
                    entity.setUpdateTime(LocalDateTime.now());

                    return entity;
                })
                .filter(entity -> !currencyRateRepository.existsById(entity.getPrimaryKey()))
                .toList();

        currencyRateRepository.saveAll(entities);
    }

}
