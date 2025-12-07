package saket.consumer.services.db_services;

import saket.consumer.model.TransactionLog;
import saket.consumer.model.Visit;
import saket.consumer.repositories.TransactionLogRepository;
import saket.consumer.repositories.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionLogService {

    private final TransactionLogRepository transactionLogRepository;
    private final VisitRepository visitRepository;

    // --- WRITE OPERATIONS ---

    @Transactional
    public TransactionLog recordTransaction(String externTxnId, BigDecimal amount, String category, Long visitId) {
        Visit visit = null;
        if (visitId != null) {
            visit = visitRepository.findById(visitId).orElse(null);
        }

        TransactionLog txn = TransactionLog.builder()
                .externTxnId(externTxnId)
                .timestamp(Instant.now())
                .amount(amount)
                .category(category)
                .visit(visit)
                .build();

        return transactionLogRepository.save(txn);
    }

    // --- READ OPERATIONS ---

    @Transactional(readOnly = true)
    public List<TransactionLog> getTransactionsForVisit(Long visitId) {
        return transactionLogRepository.findByVisitId(visitId);
    }

    @Transactional(readOnly = true)
    public TransactionLog getByExternalId(String externTxnId) {
        TransactionLog txn = transactionLogRepository.findByExternTxnId(externTxnId);
        if (txn == null) {
            throw new RuntimeException("Transaction not found for ID: " + externTxnId);
        }
        return txn;
    }
}