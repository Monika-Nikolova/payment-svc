package bg.softuni.paymentsvc.web;

import bg.softuni.paymentsvc.model.Transaction;
import bg.softuni.paymentsvc.service.TransactionService;
import bg.softuni.paymentsvc.web.dto.DtoMapper;
import bg.softuni.paymentsvc.web.dto.PaymentRequest;
import bg.softuni.paymentsvc.web.dto.PaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> makePayment(@RequestBody PaymentRequest request) {

        Transaction transaction = transactionService.processPayment(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(DtoMapper.fromTransaction(transaction));
    }

    @GetMapping
    public ResponseEntity<PaymentResponse> getPaymentById(@RequestParam UUID transactionId) {

        Transaction transaction = transactionService.getById(transactionId);

        return ResponseEntity.ok(DtoMapper.fromTransaction(transaction));
    }
}
