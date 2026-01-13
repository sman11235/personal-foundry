package saket.consumer.services;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;

import org.springframework.stereotype.Component;

import saket.consumer.domain.userFSM.UserState;

@Component
public class UserStateStore {
    private final AtomicReference<UserState> ref = new AtomicReference<>(UserState.initial());

    public UserState get() {
        return ref.get();
    }

    public void set(UserState newState) {
        ref.set(newState);
    }

    // handy for safe transitions
    public UserState update(UnaryOperator<UserState> f) {
        return ref.updateAndGet(f);
    }
}
