package saket.consumer.services.db_services;

import saket.consumer.model.HealthLog;
import saket.consumer.model.Visit;
import saket.consumer.repositories.HealthLogRepository;
import saket.consumer.repositories.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HealthLogService {

    private final HealthLogRepository healthLogRepository;
    private final VisitRepository visitRepository;

    // --- WRITE OPERATIONS ---

    @Transactional
    public HealthLog logMetric(String metricType, Double value, String unit, Long visitId) {
        Visit visit = null;
        if (visitId != null) {
            visit = visitRepository.findById(visitId).orElse(null);
        }

        HealthLog log = HealthLog.builder()
                .timestamp(Instant.now())
                .metricType(metricType)
                .val(value)
                .unit(unit)
                .visit(visit)
                .build();

        return healthLogRepository.save(log);
    }

    // --- READ OPERATIONS ---

    @Transactional(readOnly = true)
    public List<HealthLog> getMetricsForVisit(Long visitId) {
        return healthLogRepository.findByVisitId(visitId);
    }

    @Transactional(readOnly = true)
    public HealthLog getById(Long id) {
        return healthLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Health Log not found: " + id));
    }
}
