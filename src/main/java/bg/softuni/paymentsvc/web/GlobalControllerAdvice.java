package bg.softuni.paymentsvc.web;

import bg.softuni.paymentsvc.exception.ProfitReportNotFoundException;
import bg.softuni.paymentsvc.exception.TransactionNotFoundException;
import bg.softuni.paymentsvc.web.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler({ProfitReportNotFoundException.class, TransactionNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleProfitReportNotFoundException(RuntimeException e) {

        ErrorResponse dto = new ErrorResponse(LocalDateTime.now(), e.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(dto);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {

        ErrorResponse dto = new ErrorResponse(LocalDateTime.now(), e.getMessage());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(dto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {

        ErrorResponse dto = new ErrorResponse(LocalDateTime.now(), e.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(dto);
    }
}
