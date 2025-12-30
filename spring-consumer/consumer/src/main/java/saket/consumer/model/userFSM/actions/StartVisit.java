package saket.consumer.model.userFSM.actions;

import java.time.Instant;

import saket.consumer.model.userFSM.states.DiscreteState;

public record StartVisit(long placeId, Instant start) implements StateAction {
    @Override
    public ActionResult execute(StateActionContext context) {
        long visitId = context.startVisit(placeId, start);
        return new ActionResult(visitId, placeId, DiscreteState.VISITING);
    }
}
