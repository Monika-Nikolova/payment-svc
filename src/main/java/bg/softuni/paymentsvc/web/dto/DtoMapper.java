package bg.softuni.paymentsvc.web.dto;

import bg.softuni.paymentsvc.model.ProfitReport;
import bg.softuni.paymentsvc.model.Transaction;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class DtoMapper {

    public static PaymentResponse fromTransaction(Transaction transaction) {
        return PaymentResponse.builder()
                .status(transaction.getStatus().name())
                .amount(transaction.getAmount())
                .cardTier(transaction.getCardTier())
                .failureReason(transaction.getFailureReason())
                .lastFourDigitsOfCardNumber(transaction.getLastFourDigitsOfCardNumber())
                .transactionId(transaction.getId())
                .period(transaction.getPeriod())
                .subscriptionType(transaction.getSubscriptionType())
                .build();
    }

    public static ProfitReportResponse fromProfitReport(ProfitReport profitReport) {
        return ProfitReportResponse.builder()
                .id(profitReport.getId())
                .amount(profitReport.getAmount())
                .createdOn(profitReport.getCreatedOn())
                .numberOfTransactions(profitReport.getNumberOfTransactions())
                .status(profitReport.getStatus().getDisplayName())
                .build();
    }

    public static List<ProfitReportResponse> fromProfitReports(List<ProfitReport> profitReports) {
        return profitReports.stream().map(DtoMapper::fromProfitReport).collect(Collectors.toList());
    }
}
