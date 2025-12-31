package saket.consumer.domain.userFSM.states;

import saket.consumer.domain.userFSM.StateDecision;
import saket.consumer.domain.userFSM.UserLocationContext;
import saket.consumer.domain.userFSM.UserState;

public interface IUserState {
    StateDecision onLocation(UserState userContext, UserLocationContext locationContext); //takes care of visit logic, knownplace logic, and location logic.
}
