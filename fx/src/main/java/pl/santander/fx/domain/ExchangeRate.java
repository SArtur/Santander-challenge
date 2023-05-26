package pl.santander.fx.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

@Getter
@Builder
@AllArgsConstructor
public class ExchangeRate {

    @NonNull
    private final Currency from;
    @NonNull
    private final Currency to;
    @NonNull
    private final BigDecimal bid;
    @NonNull
    private final BigDecimal ask;
    @NonNull
    private final LocalDateTime timestamp;

}
