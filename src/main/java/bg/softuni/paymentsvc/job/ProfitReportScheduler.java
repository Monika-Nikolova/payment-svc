package bg.softuni.paymentsvc.job;

import bg.softuni.paymentsvc.service.ProfitReportService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ProfitReportScheduler {

    private final ProfitReportService profitReportService;

    public ProfitReportScheduler(ProfitReportService profitReportService) {
        this.profitReportService = profitReportService;
    }

    @Scheduled(cron = "0 0 0 * * 0")
    public void generateWeeklyProfitReport() {
        profitReportService.generateProfitReport();
    }
}
