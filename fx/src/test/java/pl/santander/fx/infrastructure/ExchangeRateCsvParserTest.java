package pl.santander.fx.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.santander.fx.domain.ExchangeRate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;

class ExchangeRateCsvParserTest {

    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS");
    private ExchangeRateCsvParser parser;

    @BeforeEach
    void init() {
        this.parser = new ExchangeRateCsvParser();
    }

    @Test
    void shouldParseCorrectRecords() {
        //given
        String data = """
                106, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001
                108, GBP/USD, 1.2500,1.2560,01-06-2020 12:01:02:002
                 108, wrong/USD, 1.2500,1.2560,01-06-2020 12:01:02:002
                """;

        //when
        var exchangeRates = parser.parse(data);

        //then
        assertThat(exchangeRates).hasSize(2);
        assertThat(exchangeRates.iterator().next())
                .usingRecursiveComparison().isEqualTo(createExchangeRate());
    }

    @Test
    void shouldNotParseRecordWithWrongBid() {
        //given
        String data = """
                106, EUR/USD, 1,1000,1.2000,01-06-2020 12:01:01:001
                """;

        //when
        var exchangeRates = parser.parse(data);

        //then
        assertThat(exchangeRates).hasSize(0);
    }


    @Test
    void shouldNotParseRecordWithWrongAsk() {
        //given
        String data = """
                106, EUR/USD, 1.1000,x2000,01-06-2020 12:01:01:001
                """;

        //when
        var exchangeRates = parser.parse(data);

        //then
        assertThat(exchangeRates).hasSize(0);
    }


    @Test
    void shouldNotParseRecordWithIncorrectlyFormattedDate() {
        //given
        String data = """
                106, EUR/USD, 1.1000,1.2000,01-06-2020T12:01:01:001
                """;

        //when
        var exchangeRates = parser.parse(data);

        //then
        assertThat(exchangeRates).hasSize(0);
    }


    @Test
    void shouldNotParseRecordWithIncorrectCurrencyCodes() {
        //given
        String data = """
                106, wrong/USD, 1.1000,1.2000,01-06-2020 12:01:01:001
                """;

        //when
        var exchangeRates = parser.parse(data);

        //then
        assertThat(exchangeRates).hasSize(0);
    }

    private ExchangeRate createExchangeRate() {
        return ExchangeRate.builder()
                .from(Currency.getInstance("EUR"))
                .to(Currency.getInstance("USD"))
                .timestamp(LocalDateTime.parse("01-06-2020 12:01:01:001", DATE_TIME_FORMAT))
                .ask(new BigDecimal("1.2000"))
                .bid(new BigDecimal("1.1000"))
                .build();
    }

    String msg = """
            106, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001
            104, EUR/USD, 2.1000,2.2000,01-06-2020 12:01:00:001
            107, EUR/JPY, 119.60,119.90,01-06-2020 12:01:02:002
            102, EUR/JPY, 15.60,15.90,01-06-2020 12:01:03:002
            108, GBP/USD, 1.2500,1.2560,01-06-2020 12:01:02:002
            """;
}
