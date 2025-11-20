package bg.softuni.paymentsvc.web.dto;

import bg.softuni.paymentsvc.model.Transaction;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {
    public static PaymentResponse fromTransaction(Transaction transaction) {
        return PaymentResponse.builder()
                .status(transaction.getStatus().name())
                .description(transaction.getDescription())
                .failureReason(transaction.getFailureReason())
                .build();
    }
}
