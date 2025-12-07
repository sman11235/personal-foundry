package saket.consumer.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import saket.consumer.model.HealthLog;

@Repository
public interface HealthLogRepository extends JpaRepository<HealthLog, Long> {
    List<HealthLog> findByVisitId(Long visitId);
}
