package bg.softuni.paymentsvc.web;

import bg.softuni.paymentsvc.model.ProfitReport;
import bg.softuni.paymentsvc.model.ProfitReportStatus;
import bg.softuni.paymentsvc.service.ProfitReportService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfitReportController.class)
public class ProfitReportControllerApiTest {

    @MockitoBean
    private ProfitReportService profitReportService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllProfitReports_thenInvokeGetAllFromProfitReportService_andReturnResponseEntityWithStatus200_andProfitReportResponseInBody() throws Exception {

        when(profitReportService.getAll()).thenReturn(List.of(randomProfitReport()));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/v1/reports/profit");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].amount").isNotEmpty())
                .andExpect(jsonPath("$[0].createdOn").isNotEmpty())
                .andExpect(jsonPath("$[0].numberOfTransactions").isNotEmpty())
                .andExpect(jsonPath("$[0].status").isNotEmpty())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        verify(profitReportService).getAll();
    }

    @Test
    void getLatestProfitReport_thenInvokeGetLatestProfitReport_andReturnResponseEntityWithStatus200_andProfitReportResponseInBody() throws Exception {

        when(profitReportService.getLatestProfitReport()).thenReturn(randomProfitReport());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/v1/reports/profit/latest");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("amount").isNotEmpty())
                .andExpect(jsonPath("createdOn").isNotEmpty())
                .andExpect(jsonPath("numberOfTransactions").isNotEmpty())
                .andExpect(jsonPath("status").isNotEmpty())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        verify(profitReportService).getLatestProfitReport();
    }

    @Test
    void getLatestProfitReport_andProfitReportIsNull_thenReturnResponseEntityWithStatus204() throws Exception {

        when(profitReportService.getLatestProfitReport()).thenReturn(null);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/v1/reports/profit/latest");

        mockMvc.perform(request)
                .andExpect(status().isNoContent());
        verify(profitReportService).getLatestProfitReport();
    }

    @Test
    void putChangeProfitStatus_thenInvokeChangeStatus() throws Exception {

        UUID id = UUID.randomUUID();
        when(profitReportService.changeStatus(id)).thenReturn(randomProfitReport());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put("/api/v1/reports/profit/" + id + "/status");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("amount").isNotEmpty())
                .andExpect(jsonPath("createdOn").isNotEmpty())
                .andExpect(jsonPath("numberOfTransactions").isNotEmpty())
                .andExpect(jsonPath("status").isNotEmpty())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        verify(profitReportService).changeStatus(id);
    }

    @Test
    void putChangeProfitStatus_andProfitReportServiceThrowsException() throws Exception {

        UUID id = UUID.randomUUID();
        when(profitReportService.changeStatus(id)).thenThrow(new RuntimeException("message"));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put("/api/v1/reports/profit/" + id + "/status");

        mockMvc.perform(request)
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("timestamp").isNotEmpty())
                .andExpect(jsonPath("message").isNotEmpty())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        verify(profitReportService).changeStatus(id);
    }

    private static ProfitReport randomProfitReport() {
        return ProfitReport.builder()
                .id(UUID.randomUUID())
                .amount(BigDecimal.valueOf(55))
                .createdOn(LocalDateTime.now())
                .numberOfTransactions(11)
                .status(ProfitReportStatus.NOT_PROCESSED)
                .build();
    }
}
