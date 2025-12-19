package saket.consumer.services.db_services;

import saket.consumer.model.KnownPlace;
import saket.consumer.model.Visit;
import saket.consumer.repositories.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * A class that correlates known_locations with all other activity.
 * Its basically how analysis is coordinated.
 * Visits get calculated at the end of the day via chron job.
 */
@Service
@RequiredArgsConstructor
public class VisitService {

    private final VisitRepository visitRepository;
    private final KnownPlaceService knownPlaceService;

    // --- WRITE OPERATIONS ---

    /**
     * A function that starts a visit.
     * @param placeId the corresponding known_location's id .
     * @param entryTime the starttime of the visit.
     * @return A half inited visit object.
     */
    @Transactional
    public Visit startVisit(Long placeId, Instant entryTime) {
        KnownPlace place = knownPlaceService.findById(placeId).orElseThrow();
        
        Visit visit = Visit.builder()
                .place(place)
                .entryTime(entryTime)
                .exitTime(entryTime) // Placeholder until they leave
                .build();

        return visitRepository.save(visit);
    }

    /**
     * A function that ends a visit.
     * @param placeId the corresponding known_location's id .
     * @param exitTime the end time of the visit.
     * @return A fully inited visit object.
     */
    @Transactional
    public Visit endVisit(Long visitId, Instant exitTime) {
        Visit visit = findById(visitId).get();
        visit.setExitTime(exitTime);
        return visitRepository.save(visit);
    }

    // --- READ OPERATIONS ---

    /**
     * finds by visitId.
     * @param id visitId.
     * @return optional visit object of visit_id n (n = id)
     */
    @Transactional(readOnly = true)
    public Optional<Visit> findById(Long id) {
        return visitRepository.findById(id);
    }
    
    /**
     * gets visits by known_place id
     * @param placeId the id of the known_place
     * @return list of visits for known_place of id placeId.
     */
    @Transactional(readOnly = true)
    public List<Visit> getVisitsForPlace(Long placeId) {
        return visitRepository.findByPlaceId(placeId);
    }

    /**
     * Gets visits from within a time range.
     * @param start start of the search time range.
     * @param end end of the search time range.
     * @return list of visits that start and end between [start] and [end].
     */
    @Transactional(readOnly = true)
    public List<Visit> getVisitsInTimeRange(Instant start, Instant end) {
        return visitRepository.findByEntryTimeBetween(start, end);
    }
}