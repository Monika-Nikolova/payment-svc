package bg.softuni.paymentsvc.repository;

import bg.softuni.paymentsvc.model.ProfitReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProfitReportRepository extends JpaRepository<ProfitReport, UUID> {
    List<ProfitReport> getAllByOrderByCreatedOnDesc();
}
