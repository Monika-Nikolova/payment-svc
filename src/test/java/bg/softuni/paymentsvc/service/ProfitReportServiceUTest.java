package bg.softuni.paymentsvc.service;

import bg.softuni.paymentsvc.exception.ProfitReportNotFoundException;
import bg.softuni.paymentsvc.model.ProfitReport;
import bg.softuni.paymentsvc.model.ProfitReportStatus;
import bg.softuni.paymentsvc.model.Transaction;
import bg.softuni.paymentsvc.repository.ProfitReportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
public class ProfitReportServiceUTest {

    @Mock
    private ProfitReportRepository profitReportRepository;
    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private ProfitReportService profitReportService;

    @Test
    void whenGenerateProfitReport_thenItIsPersistedInDatabase() {

        Transaction transaction = Transaction.builder()
                .amount(new BigDecimal("100"))
                .build();
        when(transactionService.getAllForLastWeek()).thenReturn(List.of(transaction));

        profitReportService.generateProfitReport();

        verify(profitReportRepository).save(any());
    }

    @Test
    void whenChangeStatus_thenChangeStatusOfProfitReport_andPersistInDatabase() {

        UUID id = UUID.randomUUID();
        ProfitReport profitReport = ProfitReport.builder()
                .status(ProfitReportStatus.NOT_PROCESSED)
                .build();
        when(profitReportRepository.findById(id)).thenReturn(Optional.of(profitReport));

        profitReportService.changeStatus(id);

        assertEquals(ProfitReportStatus.PROCESSED, profitReport.getStatus());
        verify(profitReportRepository).save(profitReport);
    }

    @Test
    void whenChangeStatus_andProfitReportWithIdWasNotFound_thenThrowException() {

        UUID id = UUID.randomUUID();
        when(profitReportRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProfitReportNotFoundException.class, () -> profitReportService.changeStatus(id));
    }
}
