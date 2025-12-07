package saket.consumer.services.db_services;

import saket.consumer.model.LocationLog;
import saket.consumer.model.Visit;
import saket.consumer.repositories.LocationLogRepository;
import saket.consumer.repositories.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationLogService {

    private final LocationLogRepository locationLogRepository;
    private final VisitRepository visitRepository;
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    // --- WRITE OPERATIONS ---

    @Transactional
    public LocationLog logLocation(String deviceId, double lat, double lon, Long visitId) {
        Point point = geometryFactory.createPoint(new Coordinate(lon, lat));

        Visit visit = null;
        if (visitId != null) {
            visit = visitRepository.findById(visitId).orElse(null);
        }

        LocationLog log = LocationLog.builder()
                .timestamp(Instant.now())
                .deviceId(deviceId)
                .loc(point)
                .visit(visit)
                .build();

        return locationLogRepository.save(log);
    }

    // --- READ OPERATIONS ---

    @Transactional(readOnly = true)
    public List<LocationLog> getLogsForVisit(Long visitId) {
        return locationLogRepository.findByVisitId(visitId);
    }
    
    @Transactional(readOnly = true)
    public LocationLog getById(Long id) {
        return locationLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location Log not found: " + id));
    }
}