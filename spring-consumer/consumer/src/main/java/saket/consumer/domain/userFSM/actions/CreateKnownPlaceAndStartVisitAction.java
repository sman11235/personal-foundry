package saket.consumer.domain.userFSM.actions;

import java.time.Instant;

import org.locationtech.jts.geom.Point;

import saket.consumer.domain.userFSM.states.DiscreteState;

public record CreateKnownPlaceAndStartVisitAction(
		Point centroid, 
		Instant start
	) implements StateAction {
		@Override
		public ActionResult execute(StateActionContext ctx) {
			long placeId = ctx.createNewKnownPlace(centroid);
			long visitId = ctx.startVisit(placeId, start);
			return new ActionResult(visitId, placeId, DiscreteState.VISITING);
		}
}

