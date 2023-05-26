package pl.santander.fx.domain;

import java.util.Collection;
import java.util.Currency;

public interface ExchangeRateRepository {

    ExchangeRate getExchangeRate(Currency from, Currency to);

    void saveExchangeRates(Collection<ExchangeRate> exchangeRates);
}
