package pl.santander.fx.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import pl.santander.fx.domain.ExchangeRate;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Utility class that parse CSV records into ExchangeRate object.
 * Assumptions:
 * <ul>
 *     <li>Delimiter: ,</li>
 *     <li>EOL character: \r\n</li>
 *     <li>Decimal point: .</li></li>
 *     <li>DateTime format: dd-MM-yyyy HH:mm:ss:SSS</li>
 *     <li>CurrencyCodes format (code according to ISO-4217): [CODE_FROM]/[CODE_TO]</li>
 *     <li>columns are in proper order: id, currencyCodes, bid, ask, timestamp</li>
 * </ul>
 */
@Slf4j
@Component
final class ExchangeRateCsvParser {

    private static final String CURRENCY_CODES_SPLIT_SIGN = "/";
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS");
    private static final CSVFormat CSV_FORMAT = CSVFormat.DEFAULT;

    Collection<ExchangeRate> parse(String data) {
        if (Objects.isNull(data)) return Collections.emptyList();
        try (CSVParser parser = CSVParser.parse(data, CSV_FORMAT)) {
            return parser.stream()
                    .map(this::parseRecord)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new ExchangeRateMessageParseException(e);
        }
    }

    private ExchangeRate parseRecord(CSVRecord record) {
        try {
            return ExchangeRate.builder()
                    .from(getFrom(record.get(Record.CURRENCY_CODES)))
                    .to(getTo(record.get(Record.CURRENCY_CODES)))
                    .ask(toBigDecimal(record.get(Record.ASK)))
                    .bid(toBigDecimal(record.get(Record.BID)))
                    .timestamp(LocalDateTime.parse(record.get(Record.TIMESTAMP), DATE_TIME_FORMAT))
                    .build();
        } catch (Throwable e) {
            log.warn("Could not parse record: {}, cause: {}", record.toString(), e.getMessage());
            return null;
        }
    }

    private Currency getFrom(String exchangeCode) {
        return getCode(exchangeCode, 0);
    }

    private Currency getTo(String exchangeCode) {
        return getCode(exchangeCode, 1);
    }

    private Currency getCode(String exchangeCode, int position) {
        var currencyCode = exchangeCode.split(CURRENCY_CODES_SPLIT_SIGN)[position].trim();
        return Currency.getInstance(currencyCode);
    }

    private BigDecimal toBigDecimal(String value) {
        return new BigDecimal(value.trim());
    }

    private final static class Record {
        static final int CURRENCY_CODES = 1;
        static final int BID = 2;
        static final int ASK = 3;
        static final int TIMESTAMP = 4;
    }

}
