package saket.consumer.services.state_dependent_behavior;

import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import saket.consumer.domain.HealthLog;
import saket.consumer.domain.Visit;
import saket.consumer.domain.userFSM.UserState;
import saket.consumer.domain.userFSM.states.DiscreteState;
import saket.consumer.repositories.VisitRepository;

@Component
public class HealthBehavior implements IHealthBehavior {
    private final VisitRepository visitRepository;

    public HealthBehavior(VisitRepository vRepo) {
        visitRepository = vRepo;
    }

    @Transactional
    @Override
    public HealthLog onHealthEvent(HealthLog event, UserState state) {
        if (state.getState() == DiscreteState.VISITING) {
            Visit currentVisit = visitRepository.getReferenceById(state.getCurrentVisit());
            event.setVisit(currentVisit);
        }
        return event;
    }
}
