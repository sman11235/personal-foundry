package saket.consumer;

import java.time.Instant;

import org.locationtech.jts.geom.Point;

import saket.consumer.domain.actions.StateActionRepository;

public class TestStateActionImpl implements StateActionRepository {
    @Override
    public long createNewKnownPlace(Point centroid) {
        System.out.println("Created new place with placeId 0");
        return 0;
    }

    @Override
    public void attachVisitToActivities(long visitId, Instant start, Instant end) {
        System.out.println("Attached visits");
    }

    @Override
    public long startVisit(long placeId, Instant start) {
        System.out.println("Started visit at placeId " + placeId + " at time " + start);
        return 0;
    }

    @Override
    public void endVisit(long visitId, Instant end) {
        System.out.println("Ended visit " + visitId);
    }
}
