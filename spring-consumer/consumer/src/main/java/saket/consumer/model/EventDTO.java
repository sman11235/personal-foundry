package saket.consumer.model;

import java.time.Instant;

import lombok.Data;
import saket.consumer.services.EventOp;
import tools.jackson.databind.JsonNode;

/**
 * A DTO that represents the kafka events that are send to this consumer.
 */
@Data
public class EventDTO {
    private String eventId;
    private String source;
    private String type;
    private EventOp op;
    private Instant observedAt;
    private JsonNode payload;
    private JsonNode attributes;
}
