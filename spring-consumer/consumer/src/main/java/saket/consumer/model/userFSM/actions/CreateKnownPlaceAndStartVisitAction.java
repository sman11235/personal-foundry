package saket.consumer.model.userFSM.actions;

import java.time.Instant;

import org.locationtech.jts.geom.Point;

import saket.consumer.model.userFSM.states.DiscreteState;

public record CreateKnownPlaceAndStartVisitAction(Point centroid, 
		Instant start, 
		String name, 
		String category
	) implements StateAction {
		@Override
		public ActionResult execute(StateActionContext ctx) {
			long placeId = ctx.createNewKnownPlace(centroid, name, category);
			long visitId = ctx.startVisit(placeId, start);
			return new ActionResult(visitId, placeId, DiscreteState.VISITING);
		}
}

