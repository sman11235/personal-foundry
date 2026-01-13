package saket.consumer.services.state_dependent_behavior;

import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.Transactional;
import saket.consumer.domain.DevLog;
import saket.consumer.domain.Visit;
import saket.consumer.domain.userFSM.UserState;
import saket.consumer.domain.userFSM.states.DiscreteState;
import saket.consumer.repositories.VisitRepository;

@Component
public class DevBehavior implements IDevBehavior {
    private final VisitRepository visitRepo;
    
    public DevBehavior(VisitRepository vRepo) {
        visitRepo = vRepo;
    }

    @Transactional
    @Override
    public DevLog onDevEvent(DevLog event, UserState state) {
        if (state.getState() == DiscreteState.VISITING) {
            Visit currentVisit = visitRepo.getReferenceById(state.getCurrentVisit());
            event.setVisit(currentVisit);
        }
        return event;
    }
}
