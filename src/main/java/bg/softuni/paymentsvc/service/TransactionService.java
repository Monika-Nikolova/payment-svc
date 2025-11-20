package bg.softuni.paymentsvc.service;

import bg.softuni.paymentsvc.model.Transaction;
import bg.softuni.paymentsvc.model.TransactionStatus;
import bg.softuni.paymentsvc.property.CardsProperties;
import bg.softuni.paymentsvc.repository.TransactionRepository;
import bg.softuni.paymentsvc.web.dto.PaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CardsProperties cardsProperties;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, CardsProperties cardsProperties) {
        this.transactionRepository = transactionRepository;
        this.cardsProperties = cardsProperties;
    }

    public Transaction processPayment(PaymentRequest paymentRequest) {

        boolean areCardDetailsValid = cardsProperties.doesCardExist(paymentRequest.getSixteenDigitCode(), paymentRequest.getDateOfExpiry(), paymentRequest.getCvvCode(), paymentRequest.getCardTier());

        TransactionStatus status = TransactionStatus.SUCCEEDED;
        String failureReason = null;

        if (!areCardDetailsValid) {
            status = TransactionStatus.FAILED;
            failureReason = "Card details are invalid";
            log.info("Payment failed for user {} with card {}", paymentRequest.getUserId(), paymentRequest.getCardTier());
        } else {
            log.info("Payment successful for user {} with card {}", paymentRequest.getUserId(), paymentRequest.getCardTier());
        }

        String description = String.format("Purchase of %s subscription",  paymentRequest.getSubscriptionType());

        return createTransaction(paymentRequest.getUserId(), paymentRequest.getAmount(), description, status, failureReason);
    }

    private Transaction createTransaction(UUID userId, BigDecimal amount, String description, TransactionStatus status, String failureReason) {

        Transaction transaction = Transaction.builder()
                .userId(userId)
                .amount(amount)
                .status(status)
                .description(description)
                .createdOn(LocalDateTime.now())
                .failureReason(failureReason)
                .build();

        transaction = transactionRepository.save(transaction);

        return transaction;
    }
}
