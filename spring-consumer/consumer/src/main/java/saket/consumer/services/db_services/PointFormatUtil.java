package saket.consumer.services.db_services;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;

import lombok.NoArgsConstructor;
import saket.consumer.exceptions.IllegalLatitudeException;
import saket.consumer.exceptions.IllegalLongitudeException;

/**
 * A util class that is in charge of centralizing boundary checking logic and point formatting.
 */
@NoArgsConstructor
@Service
public class PointFormatUtil {
    private static final int SRID_WGS84 = 4326;
    private static final GeometryFactory GF = new GeometryFactory(new PrecisionModel(), SRID_WGS84);

    /**
     * A function that returns a point in wg84 format.
     * @param lat the latitude of the point
     * @param lon the longitude of the point
     * @return a point in wg84 format (SRID 4326).
     */
    public static Point wgs84FromLatLon(double lat, double lon) {
        if (lat < -90 || lat > 90) 
            throw new IllegalLatitudeException("Latitude is either greater than 90 or less than -90, and thus invalid.");
        if (lon < -180 || lon > 180) 
            throw new IllegalLongitudeException("Longitude is either greater than 90 or less than -90, and thus invalid.");
        Point p = GF.createPoint(new Coordinate(lat, lon));
        p.setSRID(SRID_WGS84);
        return p;
    }
}
