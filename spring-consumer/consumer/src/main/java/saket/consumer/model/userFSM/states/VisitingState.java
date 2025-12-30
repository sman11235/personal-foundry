package saket.consumer.model.userFSM.states;

import java.util.List;

import saket.consumer.model.userFSM.StateDecision;
import saket.consumer.model.userFSM.UserLocationContext;
import saket.consumer.model.userFSM.UserState;
import saket.consumer.model.userFSM.actions.EndVisit;

public class VisitingState implements IUserState {
    @Override
    public StateDecision onLocation(UserState userContext, UserLocationContext locationContext) {
        if (locationContext.stationary()) {
            return new StateDecision(DiscreteState.VISITING, 
                            List.of()
                        );
        }
        return new StateDecision(DiscreteState.MOVING, 
                            List.of(new EndVisit(userContext.getCurrentVisit(), locationContext.timestamp()))
                        );
    }
}
