package bg.softuni.paymentsvc.web;

import bg.softuni.paymentsvc.model.ProfitReport;
import bg.softuni.paymentsvc.service.ProfitReportService;
import bg.softuni.paymentsvc.web.dto.DtoMapper;
import bg.softuni.paymentsvc.web.dto.ProfitReportResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reports/profit")
public class ProfitReportController {


    private final ProfitReportService profitReportService;

    public ProfitReportController(ProfitReportService profitReportService) {
        this.profitReportService = profitReportService;
    }

    @GetMapping("/latest")
    public ResponseEntity<ProfitReportResponse> getLatestProfitReport() {

        ProfitReport profitReport = profitReportService.getLatestProfitReport();

        return ResponseEntity.ok(DtoMapper.fromProfitReport(profitReport));
    }

    @GetMapping("/old")
    public ResponseEntity<List<ProfitReportResponse>> getOldProfitReports() {

        List<ProfitReport> profitReports = profitReportService.getOldProfitReports();

        return ResponseEntity.ok(DtoMapper.fromProfitReports(profitReports));
    }
}
