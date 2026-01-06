package saket.consumer.domain.actions;

import java.time.Instant;

import saket.consumer.domain.userFSM.states.DiscreteState;

/**
 * A command that ends a visit with id visitId.
 */
public record EndVisit(long visitId, Instant end) implements StateAction {
    @Override
    public ActionResult execute(StateActionContext context) {
        context.endVisit(visitId, end);
        return new ActionResult(null, null, DiscreteState.MOVING);
    }
}
