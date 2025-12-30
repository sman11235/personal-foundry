package saket.consumer.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.locationtech.jts.geom.Point;

import saket.consumer.model.LocationLog;

@Repository
public interface LocationLogRepository extends JpaRepository<LocationLog, Long> {
    List<LocationLog> findByVisitId(Long visitId);
    @Query(value = "select * from location_logs l where ST_DWithin(l.loc, :point, :radius)", 
        nativeQuery = true)
    List<LocationLog> findNearByLocations(Point point, double radius);
}
