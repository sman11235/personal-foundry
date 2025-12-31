package saket.consumer.domain.userFSM.actions;

import java.time.Instant;

import saket.consumer.domain.userFSM.states.DiscreteState;

/**
 * Starts a visit at known_place with id placeId
 */
public record StartVisit(long placeId, Instant start) implements StateAction {
    @Override
    public ActionResult execute(StateActionContext context) {
        long visitId = context.startVisit(placeId, start);
        return new ActionResult(visitId, placeId, DiscreteState.VISITING);
    }
}
