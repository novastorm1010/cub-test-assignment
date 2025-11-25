package com.example.currency.mapper;


import com.example.currency.dto.CurrencyExchangeRateDTO;
import com.example.currency.model.CurrencyExchangeRate;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CurrencyExchangeRateMapper {
    CurrencyExchangeRateDTO toDTO(CurrencyExchangeRate entity);

    List<CurrencyExchangeRateDTO> toDTOList(List<CurrencyExchangeRate> entities);
}
