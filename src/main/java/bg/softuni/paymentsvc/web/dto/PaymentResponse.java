package bg.softuni.paymentsvc.web.dto;

import bg.softuni.paymentsvc.model.CardTier;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class PaymentResponse {

    private String status;

    private BigDecimal amount;

    private CardTier cardTier;

    private String failureReason;

    private int lastFourDigitsOfCardNumber;

    private UUID transactionId;

    private String period;

    private String subscriptionType;
}
