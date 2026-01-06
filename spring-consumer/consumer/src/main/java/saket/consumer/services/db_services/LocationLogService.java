package saket.consumer.services.db_services;

import saket.consumer.domain.LocationLog;
import saket.consumer.domain.Visit;
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
import java.util.Optional;

/**
 * A class that gets the granular location data on the user.
 * Corresponds to the location_logs table in the DB.
 */
@Service
@RequiredArgsConstructor
public class LocationLogService {

    private final LocationLogRepository locationLogRepository;
    private final VisitRepository visitRepository;
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    // --- WRITE OPERATIONS ---

    /**
     * Persists a granular instance of the user's location to the DB.
     * @param deviceId the device the location was recorded on.
     * @param lat the latitude of user's location.
     * @param lon the longitude of user's location.
     * @param visitId the visit that corresponds to this location instance (nullable).
     * @return a locationlog instance.
     */
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

    /**
     * Gets location instances that correspond to a certain visit.
     * @param visitId the certain visit.
     * @return a list of location instances.
     */
    @Transactional(readOnly = true)
    public List<LocationLog> getLogsForVisit(Long visitId) {
        return locationLogRepository.findByVisitId(visitId);
    }
    
    /**
     * Gets a specific location by id. 
     * @param id the location id.
     * @return a singular location instance.
     */
    @Transactional(readOnly = true)
    public Optional<LocationLog> getById(Long id) {
        return locationLogRepository.findById(id);
    }

    /**
     * Gets all nearby LocationLogs given a search radius.
     * @param point the point from which the search will be conducted.
     * @param radius the radius from with the search circle will extend from point.
     * @return a list of LocationLogs found.
     */
    @Transactional
    public List<LocationLog> getNearbyLocations(Point point, double radius) {
        return locationLogRepository.findNearByLocations(point, radius);
    }

    /**
     * Gets all locations given a time range.
     * @param start start of the time range.
     * @param end end of the time range.
     * @return list of all locations within time range.
     */
    @Transactional
    public List<LocationLog> getLocationsFromTimeRange(Instant start, Instant end) {
        return locationLogRepository.findByTimeRange(start, end);
    }
}