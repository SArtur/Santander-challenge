package pl.santander.fx.domain;

public interface ExchangeRateService {
    ExchangeRate getExchangeRate(String currencyCodeFrom, String currencyCodeTo);
}
