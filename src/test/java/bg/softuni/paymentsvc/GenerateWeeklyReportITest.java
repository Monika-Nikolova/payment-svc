package bg.softuni.paymentsvc;

import bg.softuni.paymentsvc.job.ProfitReportScheduler;
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
public class GenerateWeeklyReportITest {

    @Autowired
    ProfitReportScheduler profitReportScheduler;

    @Autowired
    private ProfitReportService profitReportService;

    @Autowired
    private TransactionService transactionService;

    @Test
    void generateWeeklyReport_happyPath() {

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

        PaymentRequest paymentRequest2 = PaymentRequest.builder()
                .userId(UUID.randomUUID())
                .amount(BigDecimal.valueOf(55))
                .sixteenDigitCode("1002854749722846")
                .dateOfExpiry(YearMonth.of(2028, 2))
                .cvvCode("189")
                .cardTier(CardTier.VISA)
                .subscriptionType("LARGE_FARM")
                .period("YEARLY")
                .build();

        transactionService.processPayment(paymentRequest2);

        profitReportScheduler.generateWeeklyProfitReport();

        List<ProfitReport> profitReports = profitReportService.getAll();

        assertThat(profitReports).hasSize(1);
        assertEquals("110.00",  profitReports.get(0).getAmount().toString());
        assertEquals(ProfitReportStatus.NOT_PROCESSED, profitReports.get(0).getStatus());
        assertEquals(2, profitReports.get(0).getNumberOfTransactions());
    }
}
