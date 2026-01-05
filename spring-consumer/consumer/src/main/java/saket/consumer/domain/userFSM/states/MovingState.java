package saket.consumer.domain.userFSM.states;

import java.time.Duration;
import java.util.List;

import saket.consumer.domain.userFSM.StateDecision;
import saket.consumer.domain.userFSM.UserLocationContext;
import saket.consumer.domain.userFSM.UserState;
import saket.consumer.domain.userFSM.actions.CreateKnownPlaceAndStartVisitAction;
import saket.consumer.domain.userFSM.actions.StartVisit;

/**
 * This class represents the MOVING state of the user.
 * This means that the user is traveling, and not visiting a known_place.
 */
public class MovingState implements IUserState {
    
    @Override
    public DiscreteState stateName() {
        return DiscreteState.MOVING;
    }

    @Override
    public StateDecision onLocation(UserState userContext, UserLocationContext locationContext) {

        long windowLengthMins = Math.abs(Duration.between(locationContext.timestamp(), locationContext.oldestTimestampInWindow()).toMinutes());
        if (windowLengthMins <= 45 - 5) { //replace 45 with whatever constant is decided for the min window length.
            return new StateDecision(DiscreteState.START, 
                List.of()
            );
        }

        if (locationContext.stationary()) {
            if (locationContext.nearestKnownPlaceIn50m() == null) {
                return new StateDecision(DiscreteState.VISITING, 
                    List.of(
                        new CreateKnownPlaceAndStartVisitAction(
                            locationContext.centroid(),
                            locationContext.timestamp()
                        )
                    )
                );
            }
            
            return new StateDecision(DiscreteState.VISITING, 
                List.of(
                    new StartVisit(locationContext.nearestKnownPlaceIn50m().getId(), locationContext.timestamp())
                )
            );
        }

        return new StateDecision(DiscreteState.MOVING, List.of());
    }
}
