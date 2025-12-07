package saket.consumer.services;

import saket.consumer.model.EventDTO;

/**
 * An interface that defined handlers for different kafka event types.
 * Different event types can belong to the same topic.
 */
public interface ITypeStrategy {
    /**
     * A function that returns the event type it corresponds to
     * @return String that is the event type
     */
    String getTopicType();
    /**
     * A function that handles an event received from kafka.
     * @param event the event DTO from kafka.
     * @return true if success, false if fail.
     */
    boolean handle(EventDTO event);
}
