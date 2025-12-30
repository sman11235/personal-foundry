package saket.consumer.model.userFSM.actions;

import java.time.Instant;

import org.locationtech.jts.geom.Point;

/**
 * defines how StateAction can communicate with other backend infrastructure.
 */
public interface StateActionContext {
    long createNewKnownPlace(Point centroid, String name, String category);
    long startVisit(long placeId, Instant start);
    void endVisit(long visitId, Instant end);
    void attachVisitToActivities(long visitId, Instant start, Instant end);
}
