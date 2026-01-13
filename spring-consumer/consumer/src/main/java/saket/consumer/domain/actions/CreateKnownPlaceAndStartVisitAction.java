package saket.consumer.domain.actions;

import java.time.Instant;

import org.locationtech.jts.geom.Point;

import saket.consumer.domain.userFSM.states.DiscreteState;

/**
 * A command that creates a known place, names it, and starts a visit.
 */
public record CreateKnownPlaceAndStartVisitAction(
		Point centroid, 
		Instant start
	) implements StateAction {
		@Override
		public ActionResult execute(StateActionRepository ctx) {
			long placeId = ctx.createNewKnownPlace(centroid);
			long visitId = ctx.startVisit(placeId, start);
			return new ActionResult(visitId, placeId, DiscreteState.VISITING);
		}
}

