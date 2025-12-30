package saket.consumer.model.userFSM.states;

import java.util.List;

import saket.consumer.model.userFSM.StateDecision;
import saket.consumer.model.userFSM.UserLocationContext;
import saket.consumer.model.userFSM.UserState;
import saket.consumer.model.userFSM.actions.CreateKnownPlaceAndStartVisitAction;
import saket.consumer.model.userFSM.actions.StartVisit;

public class MovingState implements IUserState {

    @Override
    public StateDecision onLocation(UserState userState, UserLocationContext locationContext) {
        if (!locationContext.stationary()) {
            return new StateDecision(DiscreteState.MOVING, 
                            List.of()
                        );
        }

        if (locationContext.nearestKnownPlaceIn50m() == null) {
            return new StateDecision(DiscreteState.VISITING, 
                        List.of(
                            new CreateKnownPlaceAndStartVisitAction(
                                locationContext.centroid(),
                                locationContext.timestamp(),
                                "New Place",
                                "Unknown"
                            )
                        )
                    );
        } else {
            return new StateDecision(DiscreteState.VISITING, 
                        List.of(
                            new StartVisit(locationContext.nearestKnownPlaceIn50m().getId(), locationContext.timestamp())
                        )
                    );
        }
    }
}
