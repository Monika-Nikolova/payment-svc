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
import java.util.List;
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

        String description = String.format("Purchase of %s subscription for period %s",  paymentRequest.getSubscriptionType(), paymentRequest.getPeriod());

        Transaction transaction = createTransaction(paymentRequest, description, status, failureReason);
        transaction = transactionRepository.save(transaction);

        return transaction;
    }

    private Transaction createTransaction(PaymentRequest paymentRequest, String description, TransactionStatus status, String failureReason) {

        return Transaction.builder()
                .userId(paymentRequest.getUserId())
                .amount(paymentRequest.getAmount())
                .status(status)
                .cardTier(paymentRequest.getCardTier())
                .description(description)
                .createdOn(LocalDateTime.now())
                .failureReason(failureReason)
                .lastFourDigitsOfCardNumber(Integer.parseInt(paymentRequest.getSixteenDigitCode().substring(paymentRequest.getSixteenDigitCode().length() - 4)))
                .period(paymentRequest.getPeriod())
                .subscriptionType(paymentRequest.getSubscriptionType())
                .build();
    }

    public Transaction getById(UUID transactionId) {
        return transactionRepository.findById(transactionId).orElseThrow(() -> new RuntimeException(String.format("Transaction with id %s not found", transactionId)));
    }

    public List<Transaction> getAllForLastWeek() {
        return transactionRepository.findByCreatedOnBetweenAndStatus(LocalDateTime.now().minusWeeks(1), LocalDateTime.now(), TransactionStatus.SUCCEEDED);
    }
}
