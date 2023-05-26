package pl.santander.fx.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.santander.fx.domain.ExchangeRate;
import pl.santander.fx.domain.ExchangeRateRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Currency;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;


class ExchangeRateRepositoryImplTest {

    private ExchangeRateRepository repository;
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS");

    @BeforeEach
    void init() {
        this.repository = new ExchangeRateRepositoryImpl();
    }

    @Test
    void shouldUpdateRateInStorageWhenTimeStampIsNewer() {
        //given
        var storedExchangeRate = createExchangeRate(1);
        repository.saveExchangeRates(singletonList(storedExchangeRate));
        var exchangeRateToBeStored = createExchangeRate(5);

        //when
        repository.saveExchangeRates(singletonList(exchangeRateToBeStored));

        //then
        assertThat(repository.getExchangeRate(Currency.getInstance("PLN"), Currency.getInstance("EUR")))
                .extracting(ExchangeRate::getTimestamp)
                .isEqualTo(exchangeRateToBeStored.getTimestamp());
    }

    @Test
    void shouldNotUpdateRateInStorageWhenTimestampIsOlder() {
        //given
        var storedExchangeRate = createExchangeRate(5);
        repository.saveExchangeRates(singletonList(storedExchangeRate));
        var exchangeRateToBeStored = createExchangeRate(2);

        //when
        repository.saveExchangeRates(singletonList(exchangeRateToBeStored));

        //then
        assertThat(repository.getExchangeRate(Currency.getInstance("PLN"), Currency.getInstance("EUR")))
                .extracting(ExchangeRate::getTimestamp)
                .isEqualTo(storedExchangeRate.getTimestamp());
    }

    private ExchangeRate createExchangeRate(int plusMinutesShift) {
        return ExchangeRate.builder()
                .ask(BigDecimal.TEN)
                .bid(BigDecimal.TEN)
                .from(Currency.getInstance("PLN"))
                .to(Currency.getInstance("EUR"))
                .timestamp(LocalDateTime.now().plusMinutes(plusMinutesShift))
                .build();
    }
}
