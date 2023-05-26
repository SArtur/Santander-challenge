package pl.santander.fx.infrastructure;


import lombok.NonNull;
import org.springframework.stereotype.Repository;
import pl.santander.fx.domain.ExchangeRate;
import pl.santander.fx.domain.ExchangeRateRepository;

import java.util.Collection;
import java.util.Currency;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
class ExchangeRateRepositoryImpl implements ExchangeRateRepository {

    private final Map<String, ExchangeRate> currencyToCurrencyExchangeRateStorage = new ConcurrentHashMap<>(100);

    @Override
    public ExchangeRate getExchangeRate(@NonNull Currency from, @NonNull Currency to) {
        return Optional.ofNullable(currencyToCurrencyExchangeRateStorage.get(storageKeyOf(from, to)))
                .orElseThrow(() -> new ExchangeRateNotFoundException(from.getCurrencyCode(), to.getCurrencyCode()));
    }

    @Override
    public void saveExchangeRates(@NonNull Collection<ExchangeRate> exchangeRates) {
        exchangeRates.stream()
                .filter(this::shouldBeUpdated)
                .forEach(exchangeRate ->
                        currencyToCurrencyExchangeRateStorage.put(storageKeyOf(exchangeRate.getFrom(), exchangeRate.getTo()), exchangeRate));
    }

    private String storageKeyOf(Currency from, Currency to) {
        return String.format("%s/%s", from.getCurrencyCode(), to.getCurrencyCode());
    }

    private boolean shouldBeUpdated(ExchangeRate newExchangeRate) {
        return Optional.ofNullable(currencyToCurrencyExchangeRateStorage.get(storageKeyOf(newExchangeRate.getFrom(), newExchangeRate.getTo())))
                .map(currentExchangeRate -> newExchangeRate.getTimestamp().isAfter(currentExchangeRate.getTimestamp()))
                .orElse(Boolean.TRUE);
    }

}
