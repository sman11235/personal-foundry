package saket.consumer.model.userFSM.actions;

import java.time.Instant;

import saket.consumer.model.userFSM.states.DiscreteState;

public record EndVisit(long visitId, Instant end) implements StateAction {
    @Override
    public ActionResult execute(StateActionContext context) {
        context.endVisit(visitId, end);
        return new ActionResult(null, null, DiscreteState.MOVING);
    }
}
