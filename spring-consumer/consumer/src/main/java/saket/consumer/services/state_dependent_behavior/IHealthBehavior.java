package saket.consumer.services.state_dependent_behavior;

import saket.consumer.domain.HealthLog;
import saket.consumer.domain.userFSM.UserState;

public interface IHealthBehavior {
    HealthLog onHealthEvent(HealthLog event, UserState state);
}
