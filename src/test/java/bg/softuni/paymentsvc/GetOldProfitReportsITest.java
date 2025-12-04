package bg.softuni.paymentsvc;

import bg.softuni.paymentsvc.model.CardTier;
import bg.softuni.paymentsvc.model.ProfitReport;
import bg.softuni.paymentsvc.model.ProfitReportStatus;
import bg.softuni.paymentsvc.service.ProfitReportService;
import bg.softuni.paymentsvc.service.TransactionService;
import bg.softuni.paymentsvc.web.dto.PaymentRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
@SpringBootTest
public class GetOldProfitReportsITest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ProfitReportService profitReportService;

    @Test
    void getLatestProfitReport_thenListSizeIs1_andFieldsCorrect() {

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .userId(UUID.randomUUID())
                .amount(BigDecimal.valueOf(55))
                .sixteenDigitCode("1002854749722846")
                .dateOfExpiry(YearMonth.of(2028, 2))
                .cvvCode("189")
                .cardTier(CardTier.VISA)
                .subscriptionType("LARGE_FARM")
                .period("YEARLY")
                .build();

        transactionService.processPayment(paymentRequest);

        profitReportService.generateProfitReport();

        PaymentRequest paymentRequest2 = PaymentRequest.builder()
                .userId(UUID.randomUUID())
                .amount(BigDecimal.valueOf(5))
                .sixteenDigitCode("1002854749722846")
                .dateOfExpiry(YearMonth.of(2028, 2))
                .cvvCode("189")
                .cardTier(CardTier.VISA)
                .subscriptionType("LARGE_FARM")
                .period("MONTHLY")
                .build();

        transactionService.processPayment(paymentRequest2);

        profitReportService.generateProfitReport();

        List<ProfitReport> oldProfitReports = profitReportService.getOldProfitReports();

        assertThat(oldProfitReports).hasSize(1);
        assertEquals(ProfitReportStatus.NOT_PROCESSED, oldProfitReports.get(0).getStatus());
        assertEquals("55.00", oldProfitReports.get(0).getAmount().toString());
        assertEquals(1, oldProfitReports.get(0).getNumberOfTransactions());
    }
}
