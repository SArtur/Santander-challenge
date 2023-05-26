package pl.santander.fx.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.santander.fx.domain.ExchangeRateRepository;

@Component
@RequiredArgsConstructor
public class MsgSystemListener {


    private final ExchangeRateRepository exchangeRateRepository;
    private final ExchangeRateCsvParser parser;

    public void onMessage(String message) {
        var exchangeRates = parser.parse(message);
        exchangeRateRepository.saveExchangeRates(exchangeRates);
    }
}
