package saket.consumer.model.userFSM;

import java.time.Instant;

import org.locationtech.jts.geom.Point;

import saket.consumer.model.KnownPlace;

public record UserLocationContext (
    String deviceId,
    Instant timestamp,
    Point centroid,
    boolean stationary,
    KnownPlace nearestKnownPlaceIn50m
) {

}
