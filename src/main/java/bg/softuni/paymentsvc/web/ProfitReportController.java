package bg.softuni.paymentsvc.web;

import bg.softuni.paymentsvc.model.ProfitReport;
import bg.softuni.paymentsvc.service.ProfitReportService;
import bg.softuni.paymentsvc.web.dto.DtoMapper;
import bg.softuni.paymentsvc.web.dto.ProfitReportResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reports/profit")
public class ProfitReportController {


    private final ProfitReportService profitReportService;

    public ProfitReportController(ProfitReportService profitReportService) {
        this.profitReportService = profitReportService;
    }

    @GetMapping
    public ResponseEntity<List<ProfitReportResponse>> getAllProfitReports() {

        List<ProfitReport> profitReports = profitReportService.getAll();

        return ResponseEntity.ok(DtoMapper.fromProfitReports(profitReports));
    }

    @GetMapping("/latest")
    public ResponseEntity<ProfitReportResponse> getLatestProfitReport() {

        ProfitReport profitReport = profitReportService.getLatestProfitReport();

        if (profitReport == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(DtoMapper.fromProfitReport(profitReport));
    }

    @GetMapping("/old")
    public ResponseEntity<List<ProfitReportResponse>> getOldProfitReports() {

        List<ProfitReport> profitReports = profitReportService.getOldProfitReports();

        if (profitReports.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(DtoMapper.fromProfitReports(profitReports));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ProfitReportResponse> changeReportStatus(@PathVariable UUID id) {

        ProfitReport profitReport = profitReportService.changeStatus(id);

        return ResponseEntity.ok(DtoMapper.fromProfitReport(profitReport));
    }
}
