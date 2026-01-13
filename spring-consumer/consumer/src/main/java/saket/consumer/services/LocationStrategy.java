package saket.consumer.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import saket.consumer.domain.EventDTO;
import saket.consumer.domain.LocationLog;
import saket.consumer.domain.actions.ActionResult;
import saket.consumer.domain.userFSM.StateDecision;
import saket.consumer.domain.userFSM.UserLocationContext;
import saket.consumer.domain.userFSM.UserState;
import saket.consumer.repositories.JPAStateActionRepository;
import saket.consumer.repositories.LocationLogRepository;

@Slf4j
@Component
public class LocationStrategy implements ITypeStrategy {

    private final ObjectMapper jsonReader;
    private final LocationLogRepository locationRepo;
    private final LocationAggregationService locationAggregator;
    private final UserStateMachineService userStateService;
    private final UserStateStore userStateStore;
    private final IStateActionRunner actionRunner;
    private final JPAStateActionRepository actionRepository;

    public LocationStrategy(
        ObjectMapper jReader, 
        LocationLogRepository locationRepo,
        LocationAggregationService locationAggregator,
        UserStateMachineService userStateService,
        UserStateStore userStateStore,
        IStateActionRunner actionRunner,
        JPAStateActionRepository actionRepository
    ) {
        jsonReader = jReader;
        this.locationRepo = locationRepo;
        this.locationAggregator = locationAggregator;
        this.userStateService = userStateService;
        this.userStateStore = userStateStore;
        this.actionRunner = actionRunner;
        this.actionRepository = actionRepository;
    }

    @Override
    public String getTopicType() {
        return "saket.location";
    }

    @Transactional
    @Override
    public void handle(EventDTO event) {
        LocationLog payload = null;
        try {
            payload = jsonReader.treeToValue(event.payload(), LocationLog.class);
        } catch (JsonProcessingException e) {
            log.error("JSON payload from EventDTO was malformed in LocationStrategy.");
            return;
        }
        locationRepo.save(payload);

        UserState currentState = userStateStore.get();

        UserLocationContext userLocationCtx = locationAggregator.aggregateLocationInfo(payload.getTimestamp(), event.deviceId());

        StateDecision stateDecision = userStateService.nextState(currentState, userLocationCtx);

        List<ActionResult> actionResults = actionRunner.run(stateDecision.actions(), actionRepository);

        Optional<Long> newVisitId = Optional.empty();
        boolean closeCurrentVisit = false;

        for (var actionResult : actionResults) {
            if (newVisitId.isEmpty())
                newVisitId = Optional.ofNullable(actionResult.newOpenVisitId());
            
            if (!closeCurrentVisit)
                closeCurrentVisit = actionResult.closeCurrentVisit();
        }

        UserState newState = UserState.of(stateDecision.state(), newVisitId);

        if (closeCurrentVisit) {
            newState.setVisitNull();
        } else if (newVisitId.isEmpty()) {
            newState.setVisit(currentState.getCurrentVisit());
        }

        userStateStore.update((old) -> {
            return newState;
        });
    }
}
