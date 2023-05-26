package pl.santander.fx;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import pl.santander.fx.api.ExchangeRateController;
import pl.santander.fx.api.ExchangeRateDto;
import pl.santander.fx.infrastructure.MsgSystemListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
        "exchange-rate.scale=5",
        "exchange-rate.commission.ask-margin-percent=2",
        "exchange-rate.commission.bid-margin-percent=-2"
})
class ExchangeRateIT {

    @Autowired
    private ExchangeRateController exchangeRateController;
    @Autowired
    private MsgSystemListener msgSystemListener;
    private DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS");

    @BeforeEach
    void initStorage() {
        String msg = """
                106, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001
                106, EUR/USD, 15.1000,15.2000,01-06-2020 12:01:00:001
                107, EUR/JPY, 119.60,119.90,01-06-200 12:01:02:002
                107, EUR/JPY, 119,60,119.90,01-06-2020 12:01:02:002
                107, EUR/JPY, 119.60,119.90,01-06-2020 12:01:02:002
                108, GBP/BadCurrency, 1.2500,1.2560,01-06-2020 12:01:02:002
                """;
        msgSystemListener.onMessage(msg);
    }

    @Test
    void shouldReturnExchangeRateWithAppliedCommission() {
        //given storage initialized
        var expectedResult = createExchangeRate();

        //when
        var exchangeRate = exchangeRateController.getExchangeRate("EUR", "USD"); //TODO use restAssued/restTemplate

        //then
        System.out.println(exchangeRate.toString());
        assertThat(exchangeRate)
                .usingRecursiveComparison().isEqualTo(expectedResult);
    }

    private ExchangeRateDto createExchangeRate() {
        return ExchangeRateDto.builder()
                .ask(new BigDecimal("1.22400"))
                .bid(new BigDecimal("1.07800"))
                .from("EUR")
                .to("USD")
                .updateTime(LocalDateTime.parse("01-06-2020 12:01:01:001", DATE_TIME_FORMAT))
                .build();
    }
}
