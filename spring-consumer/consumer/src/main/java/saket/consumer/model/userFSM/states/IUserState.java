package saket.consumer.model.userFSM.states;

import saket.consumer.model.userFSM.StateDecision;
import saket.consumer.model.userFSM.UserLocationContext;
import saket.consumer.model.userFSM.UserState;

public interface IUserState {
    StateDecision onLocation(UserState userContext, UserLocationContext locationContext); //takes care of visit logic, knownplace logic, and location logic.
}
