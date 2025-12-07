package saket.consumer.services.db_services;

import saket.consumer.model.KnownPlace;
import saket.consumer.repositories.KnownPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KnownPlaceService {

    private final KnownPlaceRepository knownPlaceRepository;
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    // --- WRITE OPERATIONS ---

    @Transactional
    public KnownPlace createPlace(String name, String category, double latitude, double longitude) {
        Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        
        KnownPlace place = KnownPlace.builder()
                .name(name)
                .category(category)
                .loc(point)
                .build();
        
        return knownPlaceRepository.save(place);
    }

    // --- READ OPERATIONS ---

    @Transactional(readOnly = true)
    public List<KnownPlace> findAll() {
        return knownPlaceRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public KnownPlace findById(Long id) {
        return knownPlaceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Place not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<KnownPlace> findNearby(double latitude, double longitude, double radiusInMeters) {
        Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        return knownPlaceRepository.findNearby(point, radiusInMeters);
    }
}