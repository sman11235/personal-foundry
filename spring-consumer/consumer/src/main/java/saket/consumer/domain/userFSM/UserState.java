package saket.consumer.domain.userFSM;

import saket.consumer.domain.userFSM.states.DiscreteState;
import saket.consumer.exceptions.InvalidStateException;
/**
 * Contains the state of the user.
 * 
 * This is the Source of truth for this entire application, 
 * and the basis for the Finite State Machine the application runs upon.
 */
public class UserState {
    private DiscreteState state;
    private Long currentVisit;

    public DiscreteState getState() {
        return state;
    }

    public boolean isVisiting() {
        return currentVisit != null;
    }

    public Long getCurrentVisit() {
        return currentVisit;
    }

    public void setState(DiscreteState d) {
        if (this.isVisiting() && this.state == DiscreteState.MOVING) 
            throw new InvalidStateException("UserState cannot be visiting somewhere when DiscreteState is MOVING.");
        if (!this.isVisiting() && this.state != DiscreteState.VISITING) 
            throw new InvalidStateException("UserState must be visited somewhere when DiscreteState VISITING.");
        state = d;
    }

    public void startVisit(Long cv) {
        currentVisit = cv;
    }

    public void endVisit() {
        currentVisit = null;
    }
}
