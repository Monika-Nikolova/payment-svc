package bg.softuni.paymentsvc.service;

import bg.softuni.paymentsvc.model.CardTier;
import bg.softuni.paymentsvc.model.Transaction;
import bg.softuni.paymentsvc.model.TransactionStatus;
import bg.softuni.paymentsvc.property.CardsProperties;
import bg.softuni.paymentsvc.repository.TransactionRepository;
import bg.softuni.paymentsvc.web.dto.PaymentRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.BDDAssertions.within;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceUTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private CardsProperties cardsProperties;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void whenProcessPayment_andCardDetailsAreValid_thenCreateAndPersistInDatabaseNewSucceededTransaction() {

        UUID userId = UUID.randomUUID();
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .userId(userId)
                .amount(new BigDecimal(5))
                .sixteenDigitCode("1002854749722846")
                .cardTier(CardTier.VISA)
                .subscriptionType("Large Farm")
                .period("Monthly")
                .build();
        when(cardsProperties.doesCardExist(any(), any(), any(), any())).thenReturn(true);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

        Transaction transaction = transactionService.processPayment(paymentRequest);

        assertThat(transaction.getCreatedOn()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
        assertEquals(userId, transaction.getUserId());
        assertEquals(paymentRequest.getAmount(), transaction.getAmount());
        assertEquals(TransactionStatus.SUCCEEDED, transaction.getStatus());
        assertEquals(paymentRequest.getCardTier(), transaction.getCardTier());
        assertNull(transaction.getFailureReason());
        assertEquals(2846, transaction.getLastFourDigitsOfCardNumber());
        assertEquals(paymentRequest.getPeriod(), transaction.getPeriod());
        assertEquals(paymentRequest.getSubscriptionType(), transaction.getSubscriptionType());
        verify(transactionRepository).save(transaction);
    }

    @Test
    void whenProcessPayment_andCardDetailsAreInvalid_thenCreateAndPersistInDatabaseNewFailedTransaction() {

        UUID userId = UUID.randomUUID();
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .userId(userId)
                .amount(new BigDecimal(450))
                .sixteenDigitCode("1002854749722846")
                .cardTier(CardTier.VISA)
                .subscriptionType("Industrial Farm")
                .period("Yearly")
                .build();
        when(cardsProperties.doesCardExist(any(), any(), any(), any())).thenReturn(false);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

        Transaction transaction = transactionService.processPayment(paymentRequest);

        assertThat(transaction.getCreatedOn()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
        assertEquals(userId, transaction.getUserId());
        assertEquals(paymentRequest.getAmount(), transaction.getAmount());
        assertEquals(TransactionStatus.FAILED, transaction.getStatus());
        assertEquals(paymentRequest.getCardTier(), transaction.getCardTier());
        assertNotNull(transaction.getFailureReason());
        assertEquals(2846, transaction.getLastFourDigitsOfCardNumber());
        assertEquals(paymentRequest.getPeriod(), transaction.getPeriod());
        assertEquals(paymentRequest.getSubscriptionType(), transaction.getSubscriptionType());
        verify(transactionRepository).save(transaction);
    }
}
