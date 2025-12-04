package bg.softuni.paymentsvc.web;

import bg.softuni.paymentsvc.model.*;
import bg.softuni.paymentsvc.service.TransactionService;
import bg.softuni.paymentsvc.web.dto.PaymentRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
public class TransactionControllerApiTest {

    @MockitoBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void makePayment_thenInvokeProcessPayment_andReturnResponseEntityWithStatus201_andTransactionResponseInBody() throws Exception {

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .userId(UUID.randomUUID())
                .amount(BigDecimal.valueOf(55))
                .sixteenDigitCode("1234567890123456")
                .dateOfExpiry(YearMonth.of(2027, 7))
                .cvvCode("123")
                .cardTier(CardTier.VISA)
                .subscriptionType("LARGE_FARM")
                .period("YEARLY")
                .build();
        when(transactionService.processPayment(any())).thenReturn(randomTransaction());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/v1/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(paymentRequest));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("status").isNotEmpty())
                .andExpect(jsonPath("amount").isNotEmpty())
                .andExpect(jsonPath("cardTier").isNotEmpty())
                .andExpect(jsonPath("failureReason").isEmpty())
                .andExpect(jsonPath("lastFourDigitsOfCardNumber").isNotEmpty())
                .andExpect(jsonPath("transactionId").isNotEmpty())
                .andExpect(jsonPath("period").isNotEmpty())
                .andExpect(jsonPath("subscriptionType").isNotEmpty())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        verify(transactionService).processPayment(any());
    }

    private static Transaction randomTransaction() {
        return Transaction.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .amount(BigDecimal.valueOf(55))
                .status(TransactionStatus.SUCCEEDED)
                .cardTier(CardTier.VISA)
                .description("Some description")
                .createdOn(LocalDateTime.now())
                .lastFourDigitsOfCardNumber(1234)
                .period("YEARLY")
                .subscriptionType("LARGE_FARM")
                .build();
    }
}
