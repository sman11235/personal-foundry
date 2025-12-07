package saket.consumer.services.db_services;

import saket.consumer.model.DevLog;
import saket.consumer.model.Visit;
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

@Service
@RequiredArgsConstructor
public class DevLogService {

    private final DevLogRepository devLogRepository;
    private final VisitRepository visitRepository;
    private final ObjectMapper objectMapper;

    // --- WRITE OPERATIONS ---

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

    @Transactional(readOnly = true)
    public List<DevLog> getLogsForVisit(Long visitId) {
        return devLogRepository.findByVisitId(visitId);
    }

    @Transactional(readOnly = true)
    public DevLog getById(Long id) {
        return devLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dev Log not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<DevLog> getAll() {
        return devLogRepository.findAll();
    }
}