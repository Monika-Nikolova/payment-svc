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

        if (!areCardDetailsValid) {
            throw new RuntimeException("Card details are wrong");
        }

        String description = getDescriptionBySubscriptionType(paymentRequest.getSubscriptionType());

        makePayment(paymentRequest.getUserId(), paymentRequest.getCardTier());

        return createTransaction(paymentRequest.getUserId(), paymentRequest.getAmount(), description);
    }

    private void makePayment(UUID userId, String cardTier) {
        log.info("Payment successful for user {} with card {}", userId, cardTier);
    }

    private String getDescriptionBySubscriptionType(String subscriptionType) {
        return String.format("Purchase of %s subscription",  subscriptionType);
    }

    private Transaction createTransaction(UUID userId, BigDecimal amount, String description) {

        Transaction transaction = Transaction.builder()
                .userId(userId)
                .amount(amount)
                .status(TransactionStatus.SUCCEEDED)
                .description(description)
                .createdOn(LocalDateTime.now())
                .build();

        transaction = transactionRepository.save(transaction);

        return transaction;
    }
}
