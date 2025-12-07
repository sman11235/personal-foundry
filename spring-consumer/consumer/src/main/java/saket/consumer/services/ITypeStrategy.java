package saket.consumer.services;

import saket.consumer.model.EventDTO;

/**
 * An interface that defined handlers for different kafka event types.
 * Different event types can belong to the same topic.
 */
public interface ITypeStrategy {
    String getTopicType();
    boolean handle(EventDTO event);
}
