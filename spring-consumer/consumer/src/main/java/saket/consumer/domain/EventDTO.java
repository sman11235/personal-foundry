package saket.consumer.domain;

import java.time.Instant;

import saket.consumer.services.EventOp;
import tools.jackson.databind.JsonNode;

/**
 * A DTO that represents the kafka events that are send to this consumer.
 */
public record EventDTO(
    String eventId,
    String source,
    String type,
    EventOp op,
    Instant observedAt,
    JsonNode payload,
    JsonNode attributes
){}

