package bg.softuni.paymentsvc.web.dto;

import bg.softuni.paymentsvc.model.Transaction;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {
    public static PaymentResponse fromTransaction(Transaction transaction) {
        return PaymentResponse.builder()
                .status(transaction.getStatus().name())
                .amount(transaction.getAmount())
                .cardTier(transaction.getCardTier())
                .failureReason(transaction.getFailureReason())
                .lastFourDigitsOfCardNumber(transaction.getLastFourDigitsOfCardNumber())
                .transactionId(transaction.getId())
                .period(transaction.getPeriod())
                .subscriptionType(transaction.getSubscriptionType())
                .build();
    }
}
