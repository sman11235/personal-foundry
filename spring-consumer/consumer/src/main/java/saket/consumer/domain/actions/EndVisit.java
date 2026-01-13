package saket.consumer.domain.actions;

import java.time.Instant;

/**
 * A command that ends a visit with id visitId.
 */
public record EndVisit(long visitId, Instant end) implements StateAction {
    @Override
    public ActionResult execute(StateActionRepository context) {
        context.endVisit(visitId, end);
        return new ActionResult(null, true);
    }
}
