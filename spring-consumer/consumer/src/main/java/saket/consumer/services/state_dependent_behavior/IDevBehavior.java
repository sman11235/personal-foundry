package saket.consumer.services.state_dependent_behavior;

import saket.consumer.domain.DevLog;
import saket.consumer.domain.userFSM.UserState;

public interface IDevBehavior {
    DevLog onDevEvent(DevLog event, UserState state);
}
