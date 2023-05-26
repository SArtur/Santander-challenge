package pl.santander.fx.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;


class CommissionServiceImplTest {

    private CommissionService commissionService;

    @BeforeEach
    void init() {
        this.commissionService = new CommissionServiceImpl(createExchangeProperties());
    }

    @Test
    void shouldApplyCommissionsToBidAndAskRates() {
        //given
        var exchangeRate = createExchangeRate();

        //when
        var modifiedExchangeRate = this.commissionService.modifyRate(exchangeRate);

        //then
        assertThat(modifiedExchangeRate)
                .extracting(ExchangeRate::getBid, ExchangeRate::getAsk).asList()
                .containsExactly(new BigDecimal("9.000"), new BigDecimal("10.100"));
    }

    private ExchangeRateProperties createExchangeProperties() {
        var commission = new ExchangeRateProperties.Commission();
        commission.setAskMarginPercent("1");
        commission.setBidMarginPercent("-10");
        var exchangeRateProperties = new ExchangeRateProperties();
        exchangeRateProperties.setScale(3);
        exchangeRateProperties.setCommission(commission);
        return exchangeRateProperties;
    }

    private ExchangeRate createExchangeRate() {
        return ExchangeRate.builder()
                .ask(BigDecimal.TEN)
                .bid(BigDecimal.TEN)
                .from(Currency.getInstance("PLN"))
                .to(Currency.getInstance("EUR"))
                .timestamp(LocalDateTime.now())
                .build();
    }

}
