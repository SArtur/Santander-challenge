package pl.santander.fx.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@ConfigurationProperties(prefix = "exchange-rate")
@Getter
@Setter
public class ExchangeRateProperties {

    private int scale = 4;
    private Commission commission = new Commission();

    /**
     * Margins should be given as percentage value, e.g. 0.1 which equals to 0.1 % margin
     */
    @Setter
    static class Commission {

        private String askMarginPercent = "0";
        private String bidMarginPercent = "0";

        public BigDecimal getAskMarginPercent() {
            return new BigDecimal(askMarginPercent);
        }

        public BigDecimal getBidMarginPercent() {
            return new BigDecimal(bidMarginPercent);
        }
    }
}
