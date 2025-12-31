package saket.consumer.domain.userFSM;

import java.time.Instant;

import org.locationtech.jts.geom.Point;

import saket.consumer.domain.KnownPlace;

public record UserLocationContext (
    String deviceId,
    Instant timestamp,
    Point centroid,
    boolean stationary,
    KnownPlace nearestKnownPlaceIn50m,
    Instant oldestTimestampInWindow
) {

}
