package saket.consumer.domain.userFSM;

import saket.consumer.domain.userFSM.states.IUserState;
import saket.consumer.domain.userFSM.states.StateRegistry;

public class UserStateMachine {

    private final StateRegistry stateRegistry;

    public UserStateMachine() {
        stateRegistry = new StateRegistry();
    }

    public StateDecision nextState(UserState userState, UserLocationContext userLocationContext) {
        IUserState currentState = stateRegistry.get(userState.getState()).orElseThrow();
        return currentState.onLocation(userState, userLocationContext); 
    }
}
