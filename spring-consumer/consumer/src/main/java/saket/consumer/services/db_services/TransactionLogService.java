package saket.consumer.services.db_services;

import saket.consumer.domain.TransactionLog;
import saket.consumer.domain.Visit;
import saket.consumer.repositories.TransactionLogRepository;
import saket.consumer.repositories.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * a class that represents transactions.
 * corresponds to the table transaction_log.
 */
@Service
@RequiredArgsConstructor
public class TransactionLogService {

    private final TransactionLogRepository transactionLogRepository;
    private final VisitRepository visitRepository;

    // --- WRITE OPERATIONS ---

    /**
     * persists a transaction into the transaction_logs table.
     * @param externTxnId external transaction id (transaction id from the credit card/bank acc).
     * @param amount transaction amount in USD.
     * @param category transaction category (food, recreation, etc.)
     * @param visitId the visit this transaction was made in (nullable).
     * @return a transactionlog obj.
     */
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
    /**
     * gets transaction associated to a certain visit.
     * @param visitId id of the visit.
     * @return list of tranactions that were made during the visit.
     */
    @Transactional(readOnly = true)
    public List<TransactionLog> getTransactionsForVisit(Long visitId) {
        return transactionLogRepository.findByVisitId(visitId);
    }

    /**
     * gets transactions by the external id.
     * @param externTxnId
     * @return A single transaction with extern Id externTxnId.
     */
    @Transactional(readOnly = true)
    public Optional<TransactionLog> getByExternalId(String externTxnId) {
        TransactionLog txn = transactionLogRepository.findByExternTxnId(externTxnId);
        return Optional.of(txn);
    }
}