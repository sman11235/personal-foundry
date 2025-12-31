package saket.consumer.domain.userFSM;

import java.util.List;

import saket.consumer.domain.userFSM.actions.StateAction;
import saket.consumer.domain.userFSM.states.DiscreteState;

public record StateDecision(DiscreteState state, List<StateAction> actions) {

}
