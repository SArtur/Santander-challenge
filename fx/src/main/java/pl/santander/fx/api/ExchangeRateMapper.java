package pl.santander.fx.api;

import org.springframework.stereotype.Component;
import pl.santander.fx.domain.ExchangeRate;

@Component
class ExchangeRateMapper implements Mapper<ExchangeRate, ExchangeRateDto> {

    @Override
    public ExchangeRateDto mapToDto(ExchangeRate model) {
        return ExchangeRateDto.builder()
                .from(model.getFrom().getCurrencyCode())
                .to(model.getTo().getCurrencyCode())
                .ask(model.getAsk())
                .bid(model.getBid())
                .updateTime(model.getTimestamp())
                .build();
    }

}
