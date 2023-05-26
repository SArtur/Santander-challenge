package pl.santander.fx.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.santander.fx.domain.ExchangeRateService;

@RestController
@RequestMapping("/api/exchange-rate")
@RequiredArgsConstructor
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;
    private final ExchangeRateMapper exchangeRateMapper;

    @GetMapping
    public ExchangeRateDto getExchangeRate(@RequestParam("from") String currencyCodeFrom, @RequestParam("to") String currencyCodeTo) {
        return exchangeRateMapper.mapToDto(exchangeRateService.getExchangeRate(currencyCodeFrom, currencyCodeTo));
    }
}
