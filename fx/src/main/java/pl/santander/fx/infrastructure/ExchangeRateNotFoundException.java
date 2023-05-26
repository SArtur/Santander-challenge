package pl.santander.fx.infrastructure;

import lombok.NonNull;

public class ExchangeRateNotFoundException extends RuntimeException {

    ExchangeRateNotFoundException(@NonNull String currencyCodeFrom, @NonNull String currencyCodeTo) {
        super(String.format("Exchange rate from %s to %s not found", currencyCodeFrom, currencyCodeTo));
    }
}
