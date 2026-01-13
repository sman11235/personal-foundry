package saket.consumer.services.state_dependent_behavior;

import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.Transactional;
import saket.consumer.domain.TransactionLog;
import saket.consumer.domain.Visit;
import saket.consumer.domain.userFSM.UserState;
import saket.consumer.domain.userFSM.states.DiscreteState;
import saket.consumer.repositories.VisitRepository;

@Component
public class TransactionBehavior implements ITransactionBehavior{
    private final VisitRepository visitRepository;

    public TransactionBehavior(VisitRepository vRepo) {
        visitRepository = vRepo;
    }

    @Transactional
    @Override
    public TransactionLog onTransactionEvent(TransactionLog event, UserState state) {
        if (state.getState() == DiscreteState.VISITING) {
            Visit currentVisit = visitRepository.getReferenceById(state.getCurrentVisit());
            event.setVisit(currentVisit);
        }
        return event;
    }
}
