package saket.consumer.repositories;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import saket.consumer.domain.Visit;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByPlaceId(Long placeId);
    List<Visit> findByEntryTimeBetween(Instant start, Instant end);
}
