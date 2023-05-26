package pl.santander.fx.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
@RequiredArgsConstructor
class CommissionServiceImpl implements CommissionService {

    public static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
    private final ExchangeRateProperties properties;

    @Override
    public ExchangeRate modifyRate(ExchangeRate exchangeRate) {
        log.debug("Applying commission - ask: {}%, bid {}%", properties.getCommission().getAskMarginPercent(), properties.getCommission().getBidMarginPercent());
        return ExchangeRate.builder()
                .from(exchangeRate.getFrom())
                .to(exchangeRate.getTo())
                .ask(applyAskCommission(exchangeRate.getAsk()))
                .bid(applyBidCommission(exchangeRate.getBid()))
                .timestamp(exchangeRate.getTimestamp())
                .build();
    }

    private BigDecimal applyAskCommission(BigDecimal ask) {
        return applyCommission(ask, properties.getCommission().getAskMarginPercent());
    }

    private BigDecimal applyCommission(BigDecimal value, BigDecimal commission) {
        return value.multiply(commission).divide(ONE_HUNDRED).add(value).setScale(properties.getScale(), RoundingMode.HALF_EVEN);
    }

    private BigDecimal applyBidCommission(BigDecimal bid) {
        return applyCommission(bid, properties.getCommission().getBidMarginPercent());
    }

}
