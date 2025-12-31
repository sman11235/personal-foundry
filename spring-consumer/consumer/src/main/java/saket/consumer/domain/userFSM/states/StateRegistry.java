package saket.consumer.domain.userFSM.states;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

/**
 * This class will map DiscreteStates to implementations of IUserState.
 */
@Component
public class StateRegistry {
    private Map<DiscreteState, IUserState> registry;

    /**
     * Constructor for state registry.
     * Will initialize the registry EnumMap with injected IUserStates.
     * @param states IUserState impls that will be injected by Spring IoC container.
     */
    public StateRegistry(List<IUserState> states) {
        registry = new EnumMap<>(DiscreteState.class);
        for (IUserState state : states) {
            registry.put(state.stateName(), state);
        } 
    }

    /**
     * fetchs the corresponding IUserState impl that matches the given DiscreteState.
     * @param state the given DiscreteState.
     * @return Optional: null or the corresponding IUserState.
     */
    public Optional<IUserState> get(DiscreteState state) {
        IUserState userState = registry.get(state);
        return Optional.ofNullable(userState);
    }
}
