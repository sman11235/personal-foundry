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
import java.util.Optional;

/**
 * A class that represents an instance of the user's health data.
 * Corresponds to the health_logs table in the DB.
 */
@Service
@RequiredArgsConstructor
public class HealthLogService {

    private final HealthLogRepository healthLogRepository;
    private final VisitRepository visitRepository;

    // --- WRITE OPERATIONS ---

    /**
     * Persists an instance of the user's health data into the DB
     * @param metricType the type of health data (miles walked, heart beat).
     * @param value the value of the data (2.3 miles, 116 bpm)
     * @param unit the unit of the value (miles, bpm)
     * @param visitId the visit id of the visit the medical data was taken from (nullable).
     * @return a healthlog instance.
     */
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

    /**
     * Gets the health data associated with a visit
     * @param visitId the specific visit.
     * @return a list of health log instances.
     */
    @Transactional(readOnly = true)
    public List<HealthLog> getMetricsForVisit(Long visitId) {
        return healthLogRepository.findByVisitId(visitId);
    }

    /**
     * gets a specific health log by id.
     * @param id the id of the health log.
     * @return the specific health log.
     */
    @Transactional(readOnly = true)
    public Optional<HealthLog> getById(Long id) {
        return healthLogRepository.findById(id);
    }
}
