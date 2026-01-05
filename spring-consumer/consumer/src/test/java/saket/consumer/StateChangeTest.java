package saket.consumer;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;

import saket.consumer.domain.KnownPlace;
import saket.consumer.domain.KnownPlaceStatus;
import saket.consumer.domain.userFSM.StateDecision;
import saket.consumer.domain.userFSM.UserLocationContext;
import saket.consumer.domain.userFSM.UserState;
import saket.consumer.domain.userFSM.UserStateMachine;
import saket.consumer.domain.userFSM.actions.StateAction;
import saket.consumer.domain.userFSM.states.DiscreteState;
import saket.consumer.services.db_services.PointFormatUtil;

public class StateChangeTest {
    private UserStateMachine stateChange = new UserStateMachine();

    private static Instant timestamp = Instant.ofEpochMilli(1767273600000L);
    private static Point centroid = PointFormatUtil.wgs84FromLatLon(10, 10);
    private static KnownPlace place = new KnownPlace(100L, 
            "Tech Square", 
            "Public Area", 
            PointFormatUtil.wgs84FromLatLon(10, 10.001), 
            timestamp,
            null,
            KnownPlaceStatus.ESTABLISHED);
    

    @Test
    void startToVisitingWithPlace() {
        System.out.println("startToVisitingWithPlace");
        boolean isStationary = true;
        UserState state = new UserState(DiscreteState.START);
        UserLocationContext context = new UserLocationContext("IPHONE",
                                                              timestamp,
                                                              centroid, 
                                                              isStationary, 
                                                              place,
                                                              timestamp.minusSeconds(60 * 60));
        StateDecision dec = stateChange.nextState(state, context);
        assertThat(dec.state()).isEqualTo(DiscreteState.VISITING);
        for (StateAction s : dec.actions()) {
            s.execute(new TestStateActionImpl());
        }
        System.out.println("=================");
    }

    @Test
    void startToVisitingNoPlace() {
        System.out.println("startToVisitingNoPlace");
        boolean isStationary = true;
        UserState state = new UserState(DiscreteState.START);
        UserLocationContext context = new UserLocationContext("IPHONE",
                                                              timestamp,
                                                              centroid, 
                                                              isStationary, 
                                                              null,
                                                              timestamp.minusSeconds(60 * 60));
        StateDecision dec = stateChange.nextState(state, context);
        assertThat(dec.state()).isEqualTo(DiscreteState.VISITING);
        for (StateAction s : dec.actions()) {
            s.execute(new TestStateActionImpl());
        } 
        System.out.println("=================");
    }

    @Test
    void startToMovingNoVisit() {
        System.out.println("startToMovingNoVisit");
        boolean isStationary = false;
        UserState state = new UserState(DiscreteState.START);
        UserLocationContext context = new UserLocationContext("IPHONE",
                                                              timestamp,
                                                              centroid, 
                                                              isStationary, 
                                                              place,
                                                              timestamp.minusSeconds(60 * 60));
        StateDecision dec = stateChange.nextState(state, context);
        assertThat(dec.state()).isEqualTo(DiscreteState.MOVING);
        for (StateAction s : dec.actions()) {
            s.execute(new TestStateActionImpl());
        } 
        System.out.println("=================");
    }

    @Test
    void startToMovingWithVisit() {
        System.out.println("startToMovingWithVisit");
        boolean isStationary = false;
        UserState state = new UserState(DiscreteState.START, 15);
        UserLocationContext context = new UserLocationContext("IPHONE",
                                                              timestamp,
                                                              centroid, 
                                                              isStationary, 
                                                              place,
                                                              timestamp.minusSeconds(60 * 60));
        StateDecision dec = stateChange.nextState(state, context);
        assertThat(dec.state()).isEqualTo(DiscreteState.MOVING);
        for (StateAction s : dec.actions()) {
            s.execute(new TestStateActionImpl());
        } 
        System.out.println("=================");
    }

    @Test
    void movingToVisitingWithPlace() {
        System.out.println("movingToVisitingWithPlace");
        boolean isStationary = true;
        UserState state = new UserState(DiscreteState.MOVING);
        UserLocationContext context = new UserLocationContext("IPHONE",
                                                              timestamp,
                                                              centroid, 
                                                              isStationary, 
                                                              place,
                                                              timestamp.minusSeconds(60 * 60));
        StateDecision dec = stateChange.nextState(state, context);
        assertThat(dec.state()).isEqualTo(DiscreteState.VISITING);
        for (StateAction s : dec.actions()) {
            s.execute(new TestStateActionImpl());
        }
        System.out.println("=================");
    }

    @Test
    void movingToVisitingNoPlace() {
        System.out.println("movingToVisitingNoPlace");
        boolean isStationary = true;
        UserState state = new UserState(DiscreteState.MOVING);
        UserLocationContext context = new UserLocationContext("IPHONE",
                                                              timestamp,
                                                              centroid, 
                                                              isStationary, 
                                                              null,
                                                              timestamp.minusSeconds(60 * 60));
        StateDecision dec = stateChange.nextState(state, context);
        assertThat(dec.state()).isEqualTo(DiscreteState.VISITING);
        for (StateAction s : dec.actions()) {
            s.execute(new TestStateActionImpl());
        } 
        System.out.println("=================");
    }

    @Test
    void visitingToMovingWithNoVisitID() {
        System.out.println("visitingToMovingWithNoVisitID");
        boolean isStationary = false;
        UserState state = new UserState(DiscreteState.VISITING);
        UserLocationContext context = new UserLocationContext("IPHONE",
                                                              timestamp,
                                                              centroid, 
                                                              isStationary, 
                                                              null,
                                                              timestamp.minusSeconds(60 * 60));
        assertThrows(IllegalStateException.class, () -> stateChange.nextState(state, context));
        System.out.println("=================");
    }

    @Test
    void visitingToMovingWithVisitID() {
        System.out.println("visitingToMovingWithVisitID");
        boolean isStationary = false;
        UserState state = new UserState(DiscreteState.VISITING, 10);
        UserLocationContext context = new UserLocationContext("IPHONE",
                                                              timestamp,
                                                              centroid, 
                                                              isStationary, 
                                                              null,
                                                              timestamp.minusSeconds(60 * 60));
        StateDecision dec = stateChange.nextState(state, context);
        assertThat(dec.state()).isEqualTo(DiscreteState.MOVING);
        for (StateAction s : dec.actions()) {
            s.execute(new TestStateActionImpl());
        }
        System.out.println("=================");
    }
}
