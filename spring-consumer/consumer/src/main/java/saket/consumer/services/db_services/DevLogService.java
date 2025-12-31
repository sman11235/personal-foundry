package saket.consumer.services.db_services;

import saket.consumer.domain.DevLog;
import saket.consumer.domain.Visit;
import saket.consumer.repositories.DevLogRepository;
import saket.consumer.repositories.VisitRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A class that handles all db interactions with the dev_logs table.
 * dev_logs logs all developer active from the user (github commit, leetcode completions)
 */
@Service
@RequiredArgsConstructor
public class DevLogService {

    private final DevLogRepository devLogRepository;
    private final VisitRepository visitRepository;
    private final ObjectMapper objectMapper;

    // --- WRITE OPERATIONS ---

    /**
     * A functions that persists a devlog row to the DB.
     * @param platform The platform the dev log occurred on (github, leetcode).
     * @param actionType the actions performs (all clear for LC, commits for github)
     * @param target The objects of the action (the specific leetcode Q, the git repo user commited to).
     * @param metadataMap Metadata about the target
     * @param visitId The visit this happened on (nullable)
     * @return DevLog event
     */
    @Transactional
    public DevLog logEvent(String platform, String actionType, String target, Map<String, Object> metadataMap, Long visitId) {
        Visit visit = null;
        if (visitId != null) {
            visit = visitRepository.findById(visitId).orElse(null);
        }

        // Convert Map to JsonNode for the entity
        JsonNode jsonMetadata = objectMapper.valueToTree(metadataMap);

        DevLog log = DevLog.builder()
                .timestamp(Instant.now())
                .platform(platform)
                .actionType(actionType)
                .target(target)
                .metadata(jsonMetadata)
                .visit(visit)
                .build();

        return devLogRepository.save(log);
    }

    // --- READ OPERATIONS ---
    /**
     * gets dev activity by visit
     * @param visitId visit id
     * @return dev activity for visit n (n = visitId).
     */
    @Transactional(readOnly = true)
    public List<DevLog> getLogsForVisit(Long visitId) {
        return devLogRepository.findByVisitId(visitId);
    }

    /**
     * Gets specific dev log by dev log id.
     * @param id id of dev log.
     * @return the specified dev log.
     */
    @Transactional(readOnly = true)
    public Optional<DevLog> getById(Long id) {
        return devLogRepository.findById(id);
    }

    /**
     * gets all dev activity ever
     * @return list of all dev logs
     */
    @Transactional(readOnly = true)
    public List<DevLog> getAll() {
        return devLogRepository.findAll();
    }
}