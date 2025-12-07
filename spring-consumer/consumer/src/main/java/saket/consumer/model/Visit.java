package saket.consumer.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.List;

/**
 * A class that acts as the model for the visits column in the database.
 * Tracks instances when i've been to a certain location for a while and done actions there.
 * This is where most of the correlation logic will be acting on.
 * Ex: 30 dollars spent at Starbucks while there for 40 mins
 */
@Entity
@Table(name = "visits")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private KnownPlace place;

    @Column(name = "entry_time", nullable = false)
    private Instant entryTime;

    @Column(name = "exit_time", nullable = false)
    private Instant exitTime;

    // Bidirectional mappings for logs (Optional, but useful)
    @OneToMany(mappedBy = "visit", fetch = FetchType.LAZY)
    private List<LocationLog> locationLogs;

    @OneToMany(mappedBy = "visit", fetch = FetchType.LAZY)
    private List<TransactionLog> transactionLogs;
}
