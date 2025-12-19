package saket.consumer;
import saket.consumer.model.KnownPlace;
import saket.consumer.services.db_services.KnownPlaceService;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
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

    @Test
    void testSpatialQuery() {
        // 1. Create a place at Atlanta Tech Square (roughly)
        double techSquareLat = 33.7766;
        double techSquareLon = -84.3886;
        knownPlaceService.createPlace("Tech Square", "Work", techSquareLat, techSquareLon);

        // 2. Create a place far away (New York)
        knownPlaceService.createPlace("Times Square", "Tourism", 40.7580, -30.9855);

        // 3. Search within 500 meters of Tech Square
        List<KnownPlace> nearby = knownPlaceService.findNearby(33.7766, -84.3884, 500);

        // 4. Verify we found Tech Square but NOT Times Square
        for (KnownPlace k : nearby) {
            System.out.println(k.getName() + ": " + k.getLoc());
        }
        assertThat(nearby).hasSize(1);
        assertThat(nearby.get(0).getName()).isEqualTo("Tech Square");
    }

    @Test
    void testCloseSpatialQuery() {
        // 1. Create a place at Atlanta Tech Square (roughly)
        double techSquareLat = 33.7766;
        double techSquareLon = -84.3886;
        knownPlaceService.createPlace("Tech Square", "Work", techSquareLat, techSquareLon);

        // 2. Create a place far away (New York)
        knownPlaceService.createPlace("Times Square", "Tourism", techSquareLat + 0.25, techSquareLon - 0.25);

        // 3. Search within 500 meters of Tech Square
        List<KnownPlace> nearby = knownPlaceService.findNearby(33.7766, -84.3884, .5);

        // 4. Verify we found Tech Square but NOT Times Square
        for (KnownPlace k : nearby) {
            System.out.println(k.getName() + ": " + k.getLoc());
        }
        assertThat(nearby).hasSize(1);
        assertThat(nearby.get(0).getName()).isEqualTo("Tech Square");
    }

    @AfterAll
    static void tearDown() {
        postgis.stop();
    }
}