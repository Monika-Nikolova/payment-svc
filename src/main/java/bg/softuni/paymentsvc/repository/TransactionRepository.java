package bg.softuni.paymentsvc.repository;

import bg.softuni.paymentsvc.model.Transaction;
import bg.softuni.paymentsvc.model.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByCreatedOnBetweenAndStatus(LocalDateTime localDateTime, LocalDateTime now, TransactionStatus transactionStatus);
}
