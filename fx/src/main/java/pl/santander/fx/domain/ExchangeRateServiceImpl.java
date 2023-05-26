package pl.santander.fx.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Currency;

@Slf4j
@Service
@RequiredArgsConstructor
class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final CommissionService commissionService;

    @Override
    public ExchangeRate getExchangeRate(String currencyCodeFrom, String currencyCodeTo) {
        var exchangeRate = exchangeRateRepository.getExchangeRate(Currency.getInstance(currencyCodeFrom), Currency.getInstance(currencyCodeTo));
        log.debug("Exchange rate found for {}/{} - {}/{}", currencyCodeFrom, currencyCodeTo, exchangeRate.getBid(), exchangeRate.getAsk());
        return commissionService.modifyRate(exchangeRate);
    }

}
