package saket.consumer.domain.userFSM.actions;

import java.time.Instant;

import org.locationtech.jts.geom.Point;

/**
 * defines how StateAction can communicate with other backend infrastructure.
 */
public interface StateActionContext {
    //this function will use a geolocation service to figure out a name and category for the new place.
    long createNewKnownPlace(Point centroid);
    long startVisit(long placeId, Instant start);
    void endVisit(long visitId, Instant end);
    void attachVisitToActivities(long visitId, Instant start, Instant end);
}
