package bg.softuni.paymentsvc.property;

import bg.softuni.paymentsvc.config.YamlPropertySourceFactory;
import bg.softuni.paymentsvc.model.CardTier;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.time.YearMonth;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties
@PropertySource(value = "classpath:cards.yml", factory = YamlPropertySourceFactory.class)
public class CardsProperties {

    private List<Card> cards;

    @Data
    public static class Card {
        private String sixteenDigitCode;
        private YearMonth dateOfExpiry;
        private String cvvCode;
        private String cardTier;
    }

    public boolean doesCardExist(String sixteenDigitCode, YearMonth dateOfExpiry, String cvvCode, CardTier cardTier) {
        for (Card card : cards) {
            if (card.sixteenDigitCode.equals(sixteenDigitCode) && card.dateOfExpiry.equals(dateOfExpiry) && card.cvvCode.equals(cvvCode) && card.cardTier.equals(cardTier.name())) {
                return true;
            }
        }
        return false;
    }
}
