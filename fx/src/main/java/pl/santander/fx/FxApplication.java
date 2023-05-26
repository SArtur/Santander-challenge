package pl.santander.fx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.santander.fx.infrastructure.MsgSystemListener;

@SpringBootApplication
public class FxApplication implements CommandLineRunner {

    @Autowired
    private MsgSystemListener msgSystemListener;

    public static void main(String[] args) {
        SpringApplication.run(FxApplication.class, args);

    }

    //TODO remove. For REST call demonstrating purposes only
    @Override
    public void run(String... args) throws Exception {
        String msg = """
                106, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001
                104, EUR/USD, 2.1000,2.2000,01-06-2020 12:01:00:001
                107, EUR/JPY, 119.60,119.90,01-06-2020 12:01:02:002
                102, EUR/JPY, 15.60,15.90,01-06-2020 12:01:03:002
                108, GBP/USD, 1.2500,1.2560,01-06-2020 12:01:02:002
                """;
        msgSystemListener.onMessage(msg);
    }
}
