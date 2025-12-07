package saket.consumer.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.locationtech.jts.geom.Point;

import saket.consumer.model.KnownPlace;

@Repository
public interface KnownPlaceRepository extends JpaRepository<KnownPlace, Long> {
    @Query(value = "SELECT * FROM known_places k " +
                   "WHERE ST_DWithin(k.loc, :point, :radiusInMeters)", 
           nativeQuery = true)
    List<KnownPlace> findNearby(Point point, double radiusInMeters);
}
