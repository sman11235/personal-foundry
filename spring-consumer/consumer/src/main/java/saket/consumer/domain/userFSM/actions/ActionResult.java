package saket.consumer.domain.userFSM.actions;

import saket.consumer.domain.userFSM.states.DiscreteState;

public record ActionResult(Long visitId, Long placeId, DiscreteState forcedState) {
    public static ActionResult emptyResult() {
        return new ActionResult(null, null, null);
    }
}
