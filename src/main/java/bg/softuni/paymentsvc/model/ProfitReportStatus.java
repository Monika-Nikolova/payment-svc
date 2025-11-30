package bg.softuni.paymentsvc.model;

import lombok.Getter;

@Getter
public enum ProfitReportStatus {
    NOT_PROCESSED("Not Processed"),
    PROCESSED("Processed"),;

    private final String displayName;

    ProfitReportStatus(String displayName) {
        this.displayName = displayName;
    }
}
