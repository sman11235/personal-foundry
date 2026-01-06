package saket.consumer.services;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import saket.consumer.domain.KnownPlace;
import saket.consumer.domain.LocationLog;
import saket.consumer.domain.userFSM.UserLocationContext;
import saket.consumer.services.db_services.KnownPlaceService;
import saket.consumer.services.db_services.LocationLogService;
import saket.consumer.services.db_services.PointUtil;

/**
 * A service that contains the logic that enriches raw LocationLogs into
 * useful UserLocationContexts. 
 */
@Service
public class LocationAggregationService {
    private final LocationLogService locationServ;
    private final KnownPlaceService placeServ;

    public LocationAggregationService(LocationLogService l, KnownPlaceService k) {
        locationServ = l;
        placeServ = k;
    }

    /**
     * Aggregates the raw LocationLog information into an immutable UserLocationContext object.
     * @param currentTime the timestamp of the last inserted LocationLog.
     * @param deviceId the id of the device that sent the location log.
     * @return UserLocationContext
     */
    public UserLocationContext aggregateLocationInfo(Instant currentTime, String deviceId) {
        List<LocationLog> window = getWindow(currentTime, Constants.WINDOW_DURATION_MINS);
        List<Point> points = window.stream().map(LocationLog::getLoc).toList();

        Optional<Point> centroid = PointUtil.centroid(points);
        if (centroid.isEmpty()) return UserLocationContext.empty();

        Optional<Double> maxDistanceFromCentroid = maxDistanceFromCentroid(points, centroid.get());
        boolean stationary = maxDistanceFromCentroid.get() <= Constants.STATIONARY_RADIUS_M;

        KnownPlace closestKnownPlace = getClosestKnownPlaceInRadius(centroid.get(), Constants.KNOWN_PLACE_MATCH_RADIUS_M).get();

        Instant oldestTimestamp = getOldestTimestampInWindow(window);

        return new UserLocationContext(deviceId, currentTime, centroid.get(), stationary, closestKnownPlace, oldestTimestamp);
    }

    /**
     * Gets the locationlog window
     * @param currentTime the timestamp of the last inserted LocationLog.
     * @param windowLengthMins The temporal length of the window.
     * @return List of location logs.
     */
    private List<LocationLog> getWindow(Instant currentTime, long windowLengthMins) {
        long seconds = windowLengthMins * 60;
        return locationServ.getLocationsFromTimeRange(currentTime.minusSeconds(seconds), currentTime);
    }

    /**
     * Gets the closest known_place from point centroid, iff the known_place if within radius meters of centroid.
     * @param centroid the central point from which the search will be conducted.
     * @param radius the radius of the search.
     * @return the known_place.
     */
    private Optional<KnownPlace> getClosestKnownPlaceInRadius(Point centroid, double radius) {
        List<KnownPlace> nearby = placeServ.findNearby(centroid, radius);
        if (nearby.isEmpty()) {
            return Optional.empty();
        } else if (nearby.size() == 1) {
            return Optional.of(nearby.get(0));
        } else {
            nearby.sort(Comparator.comparingDouble(
                p -> PointUtil.distanceInMeters(p.getLoc(), centroid)
            ));
            return Optional.of(nearby.get(0));
        }
    }

    /**
     * Gets the distance of the point in window that is furthest away from point. 
     * @param window the list of points to be compared against point.
     * @param point the point that distance will be calculated from.
     * @return the furthest distance in window from point.
     */
    private Optional<Double> maxDistanceFromCentroid(List<Point> window, Point centroid) {
        if (window.isEmpty()) {
            return Optional.empty();
        }
        double max = 0.0;
        for (Point p : window) {
            if (p == null) continue;
            double d = PointUtil.distanceInMeters(p, centroid);
            if (d > max) max = d;
        }
        return Optional.of(max);
    }

    /**
     * gets the oldests timestamp from the given window.
     * @param window the window of LocationLogs
     * @return the oldest timestamp.
     */
    private Instant getOldestTimestampInWindow(List<LocationLog> window) {
        Instant oldest = Instant.MAX;
        for (LocationLog l : window) {
            if (l.getTimestamp().isBefore(oldest)) {
                oldest = l.getTimestamp();
            }
        }
        return oldest;
    }
}
