package pl.santander.fx.infrastructure;

import java.io.IOException;

public class ExchangeRateMessageParseException extends RuntimeException {
    public ExchangeRateMessageParseException(IOException e) {
        super("Could not parse exchange rate message", e);
    }

}
