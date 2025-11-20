package bg.softuni.paymentsvc.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponse {

    private String status;

    private String description;

    private String failureReason;
}
