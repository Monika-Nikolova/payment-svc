package bg.softuni.paymentsvc.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.UUID;

@Data
@Builder
public class PaymentRequest {

    @NotBlank
    private UUID userId;

    @NotBlank
    private BigDecimal amount;

    @NotBlank
    @Size(min = 16, max = 16)
    @Pattern(regexp = "\\d+", message = "Only digits allowed")
    private String sixteenDigitCode;

    @NotBlank
    private YearMonth dateOfExpiry;

    @NotBlank
    @Size(min = 3, max = 4)
    @Pattern(regexp = "\\d+", message = "Only digits allowed")
    private String cvvCode;

    @NotNull
    private String cardTier;

    private String subscriptionType;
}
