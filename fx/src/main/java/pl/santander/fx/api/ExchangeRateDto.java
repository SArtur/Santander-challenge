package pl.santander.fx.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class ExchangeRateDto {

    private final String from;
    private final String to;
    private final BigDecimal bid;
    private final BigDecimal ask;
    private final LocalDateTime updateTime;

}
