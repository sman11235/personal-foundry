package saket.consumer.domain.actions;

import saket.consumer.domain.userFSM.states.DiscreteState;

/**
 * The result of a state changing action action.
 * @param newOpenVisitId if the StateAction results in a open visit, then the visit's ID. else null.
 * @param newlyCreatedPlaceId of the StateAction creates a new known_place, then it's ID. else null.
 * @param nextState The next expected DiscreteState of the state machine.
 */
public record ActionResult(Long newOpenVisitId, Long newlyCreatedPlaceId, DiscreteState nextState) {
    public static ActionResult emptyResult() {
        return new ActionResult(null, null, null);
    }
}
