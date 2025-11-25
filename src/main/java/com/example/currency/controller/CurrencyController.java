package com.example.currency.controller;


import com.example.currency.common.MessageUtils;
import com.example.currency.dto.CurrencyExchangeRateDTO;
import com.example.currency.dto.CurrencyExchangeRateExternalDTO;
import com.example.currency.error.response.ErrorResponse;
import com.example.currency.service.CurrencyExchangeRateService;
import com.example.currency.service.ExternalExchangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/currencies")
public class CurrencyController {

    private final CurrencyExchangeRateService currencyExchangeRateService;
    private final ExternalExchangeService externalExchangeService;


    @Operation(summary = "Get public external currency exchange rates by base currency and date range",
            description = "Returns list of currency exchange rates filtered by base currency and close time between startDate and endDate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CurrencyExchangeRateDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid date range",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/external")
    public ResponseEntity<?> getCurrencyExchangeRateExternal(
            @Parameter(description = "Base currency code", required = true)
            @RequestParam(name = "baseCurrency") String baseCurrency,

            @Parameter(description = "Start date (yyyy-MM-dd)", required = true)
            @RequestParam(name = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,

            @Parameter(description = "End date (yyyy-MM-dd)", required = true)
            @RequestParam(name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,

            Locale locale) {

        if (startDate.isAfter(endDate) || startDate.isEqual(endDate)) {

            ErrorResponse error = ErrorResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message(MessageUtils.getMessage("INVALID_DATE_MESSAGE", locale.toString()))
                    .description(MessageUtils.getMessage("INVALID_DATE_DESCRIPTION", locale.toString()))
                    .build();

            return ResponseEntity.badRequest().body(error);
        }

        List<CurrencyExchangeRateExternalDTO> result = externalExchangeService.getCurrencyRates(baseCurrency, startDate, endDate);

        return ResponseEntity.ok(result);
    }



    @Operation(summary = "Get currency exchange rates by base currency and date range",
            description = "Returns list of currency exchange rates filtered by base currency and close time between startDate and endDate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CurrencyExchangeRateDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid date range",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping()
    public ResponseEntity<?> getCurrencyExchangeRate(
            @Parameter(description = "Base currency code", required = true)
            @RequestParam(name = "baseCurrency") String baseCurrency,

            @Parameter(description = "Start date (yyyy-MM-dd)", required = true)
            @RequestParam(name = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,

            @Parameter(description = "End date (yyyy-MM-dd)", required = true)
            @RequestParam(name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,

            @Parameter(hidden = true)
            Locale locale) {
        if (startDate.isAfter(endDate) || startDate.isEqual(endDate)) {

            ErrorResponse error = ErrorResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message(MessageUtils.getMessage("INVALID_DATE_MESSAGE", locale.toString()))
                    .description(MessageUtils.getMessage("INVALID_DATE_DESCRIPTION", locale.toString()))
                    .build();

            return ResponseEntity.badRequest().body(error);
        }

        List<CurrencyExchangeRateDTO> result =
                currencyExchangeRateService.findCurrencyExchangeRate(baseCurrency, startDate, endDate);

        return ResponseEntity.ok(result);
    }

    @PostMapping
    @Operation( summary = "Insert a new currency exchange rate",
                description = "Add a new currency exchange rate record. Throws error if the record already exists."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully inserted",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CurrencyExchangeRateDTO.class))),
            @ApiResponse(responseCode = "409", description = "Record already exists")
    })
    public ResponseEntity<CurrencyExchangeRateDTO> insert(
            @Parameter(description = "Currency exchange rate data", required = true)
            @RequestBody CurrencyExchangeRateDTO dto) {
        CurrencyExchangeRateDTO result = currencyExchangeRateService.insertCurrency(dto);
        return ResponseEntity.ok(result);
    }

    @PutMapping
    @Operation( summary = "Update an existing currency exchange rate",
                description = "Update an existing currency exchange rate record. Throws error if the record does not exist."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CurrencyExchangeRateDTO.class))),
            @ApiResponse(responseCode = "404", description = "Record not found")
    })
    public ResponseEntity<CurrencyExchangeRateDTO> update(
            @Parameter(description = "Currency exchange rate data", required = true)
            @RequestBody CurrencyExchangeRateDTO dto) {
        CurrencyExchangeRateDTO result = currencyExchangeRateService.updateCurrency(dto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping
    @Operation( summary = "Delete a currency exchange rate",
                description = "Delete a currency exchange rate record by base currency, quote currency and exchange date."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Record not found")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Exchange date in yyyy-MM-dd format", required = true)
            @RequestParam(name = "exchangeDate")  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate exchangeDate,

            @Parameter(description = "Base currency code", required = true)
            @RequestParam(name = "baseCurrency") String baseCurrency,

            @Parameter(description = "Quote currency code", required = true)
            @RequestParam(name = "quoteCurrency") String quoteCurrency) {

        currencyExchangeRateService.deleteCurrency(exchangeDate, baseCurrency, quoteCurrency);
        return ResponseEntity.noContent().build();
    }

}
