package saket.consumer.registries;

import saket.consumer.model.TransactionLog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {
    List<TransactionLog> findByVisitId(Long visitId);
    TransactionLog findByExternTxnId(String externTxnId);
}
