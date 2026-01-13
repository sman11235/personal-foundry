package saket.consumer.services.state_dependent_behavior;

import saket.consumer.domain.TransactionLog;
import saket.consumer.domain.userFSM.UserState;

public interface ITransactionBehavior {
    TransactionLog onTransactionEvent(TransactionLog event, UserState state);
}
