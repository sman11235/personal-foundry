package saket.consumer.services.db_services;

import saket.consumer.model.KnownPlace;
import saket.consumer.repositories.KnownPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * A class that handles all db interactions with the known_places table.
 */
@Service
@RequiredArgsConstructor
public class KnownPlaceService {


    private final KnownPlaceRepository knownPlaceRepository;
    // --- WRITE OPERATIONS ---


    /**
     * A functions that creates a DB entry for a place the user stays at.
     * @param name the name of the place.
     * @param category the category of place (home, leisure, shopping, ...)
     * @param latitude the latitude of the place.
     * @param longitude the longitude of the place.
     * @return a KnownPlace object that was save to DB.
     */
    @Transactional
    public KnownPlace createPlace(String name, String category, double latitude, double longitude) {
        Point point = PointFormatUtil.wgs84FromLatLon(latitude, longitude);
        KnownPlace place = KnownPlace.builder()
                .name(name)
                .category(category)
                .loc(point)
                .build();
        
        return knownPlaceRepository.save(place);
    }

    // --- READ OPERATIONS ---

    /**
     * Lists all known places
     */
    @Transactional(readOnly = true)
    public List<KnownPlace> findAll() {
        return knownPlaceRepository.findAll();
    }
    
    /**
     * Finds a place by its unique id
     * @param id the unique id (long)
     */
    @Transactional(readOnly = true)
    public Optional<KnownPlace> findById(Long id) {
        return knownPlaceRepository.findById(id);
    }

    /**
     * Finds known places within a radius of meters.
     * @param latitude latitude of the search point
     * @param longitude longitude of the search point.
     * @param radiusInMeters the radius of the search circle.
     * @return returns the list of places found within the search circle.
     */
    @Transactional(readOnly = true)
    public List<KnownPlace> findNearby(double latitude, double longitude, double radiusInMeters) {
        Point point = PointFormatUtil.wgs84FromLatLon(latitude, longitude);
        return knownPlaceRepository.findNearby(point, radiusInMeters);
    }
}