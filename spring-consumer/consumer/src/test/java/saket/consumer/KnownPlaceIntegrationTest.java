package saket.consumer;
import saket.consumer.domain.KnownPlace;
import saket.consumer.services.db_services.KnownPlaceService;
import saket.consumer.services.db_services.PointUtil;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ConsumerApplication.class)
@Testcontainers
class KnownPlaceIntegrationTest {

    // Spin up a Postgres 16 container with PostGIS pre-installed
    // Note: You must use a postgis image, not standard postgres
    private static final DockerImageName POSTGIS_IMAGE =
      DockerImageName.parse("postgis/postgis:16-3.4-alpine")
          .asCompatibleSubstituteFor("postgres");
        
    @Container
    static PostgreSQLContainer<?> postgis = 
        new PostgreSQLContainer<>(
            POSTGIS_IMAGE
        ).withDatabaseName("testDb")
         .withUsername("appDb")
         .withPassword("appDb")
         .withInitScript("01-init.sql");

    @DynamicPropertySource
    static void dbProps(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", postgis::getJdbcUrl);
        r.add("spring.datasource.username", postgis::getUsername);
        r.add("spring.datasource.password", postgis::getPassword);
        // usually not needed, but if you want:
        r.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");

    }

    @Autowired
    private KnownPlaceService knownPlaceService;

    private static final double TECH_SQUARE_LAT = 33.7766;
    private static final double TECH_SQUARE_LON = -84.3886;
    private static final int NUM_OF_POINTS = 4;


    @Test
    void testSpatialQuery() {
        knownPlaceService.createPlace("Tech Square", "Work", TECH_SQUARE_LAT, TECH_SQUARE_LON);
        knownPlaceService.createPlace("Times Square", "Tourism", 40.7580, -30.9855);
        //~27735 meters from tech square
        knownPlaceService.createPlace("Biggs Square", "Tourism", TECH_SQUARE_LAT + 0.25, TECH_SQUARE_LON);
        //30 meters from tech square.
        knownPlaceService.createPlace("Tech Square Pizza", "Food", 33.776870, -84.388600);
        Point point = PointUtil.wgs84FromLatLon(TECH_SQUARE_LAT, TECH_SQUARE_LON);
        List<KnownPlace> nearby = knownPlaceService.findNearby(point, 30000);


        assertThat(nearby).hasSize(NUM_OF_POINTS - 1);
        assertThat(nearby.get(0).getName()).isEqualTo("Tech Square");
    }

    @Test
    void testCloseSpatialQuery() {

        // 3. Search within 500 meters of Tech Square
        Point point = PointUtil.wgs84FromLatLon(TECH_SQUARE_LAT, TECH_SQUARE_LON);
        List<KnownPlace> nearby = knownPlaceService.findNearby(point, 30);

        assertThat(nearby).hasSize(NUM_OF_POINTS - 2);
        assertThat(nearby.get(0).getName()).isEqualTo("Tech Square");
        assertThat(nearby.get(1).getName()).isEqualTo("Tech Square Pizza");
    }

    @Test
    void testBorderSpatialQuery() {

        // 3. Search within 500 meters of Tech Square
        //on the border between biggs and tech square.
        Point point = PointUtil.wgs84FromLatLon(TECH_SQUARE_LAT, TECH_SQUARE_LON);
        List<KnownPlace> nearby = knownPlaceService.findNearby(point, 27725); 

        // 4. Verify we found Tech Square but NOT Times Square
        for (KnownPlace k : nearby) {
            System.out.println(k.getName() + ": " + k.getLoc());
        }
        assertThat(nearby).hasSize(NUM_OF_POINTS - 2);

        assertThat(nearby.get(0).getName()).isEqualTo("Tech Square");
        assertThat(nearby.get(1).getName()).isEqualTo("Tech Square Pizza");    }

    @AfterAll
    static void tearDown() {
        postgis.stop();
    }
}