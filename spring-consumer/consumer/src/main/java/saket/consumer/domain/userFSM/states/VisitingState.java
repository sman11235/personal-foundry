package saket.consumer.domain.userFSM.states;

import java.time.Duration;
import java.util.List;

import saket.consumer.domain.userFSM.StateDecision;
import saket.consumer.domain.userFSM.UserLocationContext;
import saket.consumer.domain.userFSM.UserState;
import saket.consumer.domain.userFSM.actions.EndVisit;

public class VisitingState implements IUserState {
    @Override
    public StateDecision onLocation(UserState userContext, UserLocationContext locationContext) {
        long windowLengthMins = Duration.between(locationContext.timestamp(), locationContext.oldestTimestampInWindow()).toMinutes();
        if (windowLengthMins <= 45 - 5) { //replace 45 with whatever constant is decided for the min window length.
            return new StateDecision(DiscreteState.START, 
                List.of()
            );
        }
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
