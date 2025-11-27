package bg.softuni.paymentsvc.service;

import bg.softuni.paymentsvc.model.ProfitReport;
import bg.softuni.paymentsvc.model.Transaction;
import bg.softuni.paymentsvc.repository.ProfitReportRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfitReportService {


    private final ProfitReportRepository profitReportRepository;
    private final TransactionService transactionService;

    public ProfitReportService(ProfitReportRepository profitReportRepository, TransactionService transactionService) {
        this.profitReportRepository = profitReportRepository;
        this.transactionService = transactionService;
    }

    public void generateProfitReport() {

        List<Transaction> transactions = transactionService.getAllForLastWeek();

        BigDecimal totalProfit = BigDecimal.ZERO;
        for (Transaction transaction : transactions) {
            totalProfit = totalProfit.add(transaction.getAmount());
        }

        ProfitReport profitReport = ProfitReport.builder()
                .amount(totalProfit)
                .createdOn(LocalDateTime.now())
                .numberOfTransactions(transactions.size())
                .build();

        profitReportRepository.save(profitReport);
    }

    public ProfitReport getLatestProfitReport() {
        return profitReportRepository.getAllByOrderByCreatedOnDesc().stream().findFirst().orElse(null);
    }

    public List<ProfitReport> getOldProfitReports() {
        return profitReportRepository.getAllByOrderByCreatedOnDesc().stream().skip(1).collect(Collectors.toList());
    }
}
