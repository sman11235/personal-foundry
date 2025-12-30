package saket.consumer.model.userFSM;

import java.util.List;

import saket.consumer.model.userFSM.actions.StateAction;
import saket.consumer.model.userFSM.states.DiscreteState;

public record StateDecision(DiscreteState state, List<StateAction> actions) {

}
