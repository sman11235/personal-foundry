package saket.consumer.services.db_services;

import saket.consumer.model.KnownPlace;
import saket.consumer.model.Visit;
import saket.consumer.repositories.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitService {

    private final VisitRepository visitRepository;
    private final KnownPlaceService knownPlaceService;

    // --- WRITE OPERATIONS ---

    @Transactional
    public Visit startVisit(Long placeId, Instant entryTime) {
        KnownPlace place = knownPlaceService.findById(placeId);
        
        Visit visit = Visit.builder()
                .place(place)
                .entryTime(entryTime)
                .exitTime(entryTime) // Placeholder until they leave
                .build();

        return visitRepository.save(visit);
    }

    @Transactional
    public Visit endVisit(Long visitId, Instant exitTime) {
        Visit visit = findById(visitId);
        visit.setExitTime(exitTime);
        return visitRepository.save(visit);
    }

    // --- READ OPERATIONS ---

    @Transactional(readOnly = true)
    public Visit findById(Long id) {
        return visitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visit not found with id: " + id));
    }
    
    @Transactional(readOnly = true)
    public List<Visit> getVisitsForPlace(Long placeId) {
        return visitRepository.findByPlaceId(placeId);
    }

    @Transactional(readOnly = true)
    public List<Visit> getVisitsInTimeRange(Instant start, Instant end) {
        return visitRepository.findByEntryTimeBetween(start, end);
    }
}