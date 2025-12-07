package saket.consumer.model;

import jakarta.persistence.*;
import lombok.*;

import org.locationtech.jts.geom.Point;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;
import java.util.List;

/**
 * A class that acts as the model for the known_place table in the database.
 * Tracks known discrete locations I have traveled to (Starbucks, library, etc.)
 */
@Entity
@Table(name = "known_places")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KnownPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String category;

    // Maps to GEOMETRY(POINT, 4326)
    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point loc;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "place", fetch = FetchType.LAZY)
    private List<Visit> visits;
}
