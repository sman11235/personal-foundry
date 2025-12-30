package saket.consumer.model.userFSM.actions;

import saket.consumer.model.userFSM.states.DiscreteState;

public record ActionResult(Long visitId, Long placeId, DiscreteState forcedState) {
    public static ActionResult emptyResult() {
        return new ActionResult(null, null, null);
    }
}
