package com.example.currency.mapper;

import com.example.currency.dto.CurrencyExchangeRateDTO;
import com.example.currency.model.CurrencyExchangeRate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-25T09:11:30+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class CurrencyExchangeRateMapperImpl implements CurrencyExchangeRateMapper {

    @Override
    public CurrencyExchangeRateDTO toDTO(CurrencyExchangeRate entity) {
        if ( entity == null ) {
            return null;
        }

        CurrencyExchangeRateDTO currencyExchangeRateDTO = new CurrencyExchangeRateDTO();

        return currencyExchangeRateDTO;
    }

    @Override
    public List<CurrencyExchangeRateDTO> toDTOList(List<CurrencyExchangeRate> entities) {
        if ( entities == null ) {
            return null;
        }

        List<CurrencyExchangeRateDTO> list = new ArrayList<CurrencyExchangeRateDTO>( entities.size() );
        for ( CurrencyExchangeRate currencyExchangeRate : entities ) {
            list.add( toDTO( currencyExchangeRate ) );
        }

        return list;
    }
}
