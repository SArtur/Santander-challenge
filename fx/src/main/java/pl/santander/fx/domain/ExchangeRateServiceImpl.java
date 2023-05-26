package pl.santander.fx.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Currency;


@Service
@RequiredArgsConstructor
class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final CommissionService commissionService;

    @Override
    public ExchangeRate getExchangeRate(String currencyCodeFrom, String currencyCodeTo) {
        var exchangeRate = exchangeRateRepository.getExchangeRate(Currency.getInstance(currencyCodeFrom), Currency.getInstance(currencyCodeTo));
        return commissionService.modifyRate(exchangeRate);
    }

}
