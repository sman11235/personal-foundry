package saket.consumer.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import saket.consumer.domain.DevLog;

@Repository
public interface DevLogRepository extends JpaRepository<DevLog, Long> {
    List<DevLog> findByVisitId(Long visitId);
}
