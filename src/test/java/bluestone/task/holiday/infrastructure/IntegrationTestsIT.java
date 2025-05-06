package bluestone.task.holiday.infrastructure;

import bluestone.task.holiday.api.CommonHolidayResponse;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ClasspathFileSource;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.standalone.JsonFileMappingsSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTestsIT {

     static WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration
            .options()
            .port(5833));
    @LocalServerPort
    int port;

    @BeforeAll
    public static void setUp() {
        wireMockServer.loadMappingsUsing(new JsonFileMappingsSource(new ClasspathFileSource(IntegrationTestsIT.class.getClassLoader(), "wiremock"), null));
        wireMockServer.start();
    }

    @AfterAll
    public static void tearDown() {
        wireMockServer.stop();
    }


    @Test
    void successfulRequest_NewYearReturn() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        String url = "http://localhost:%d/firstCommonHoliday?date=%s&countryCode1=%s&countryCode2=%s".formatted(port, "2025-01-01", "PL", "DE");
        CommonHolidayResponse result = restTemplate.getForObject(url, CommonHolidayResponse.class);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Nowy Rok", result.name1());
        Assertions.assertEquals("Neujahr", result.name2());
        Assertions.assertEquals(LocalDate.of(2025, 1, 1), result.date());
    }

    @Test
    void successfulRequest_noMatch() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        String url = "http://localhost:%d/firstCommonHoliday?date=%s&countryCode1=%s&countryCode2=%s".formatted(port, "2025-01-01", "PL", "ES");
        ResponseEntity<Void> forObject = restTemplate.getForEntity(url, Void.class);
        Assertions.assertEquals(HttpStatusCode.valueOf(404), forObject.getStatusCode());
    }

    @Test
    void unsuccessfulRequest_unsupportedCountry() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        String url = "http://localhost:%d/firstCommonHoliday?date=%s&countryCode1=%s&countryCode2=%s".formatted(port, "2025-01-01", "US", "ES");
        ResponseEntity<ProblemDetail> forObject = restTemplate.getForEntity(url, ProblemDetail.class);
        Assertions.assertEquals(HttpStatusCode.valueOf(400), forObject.getStatusCode());
    }

    @Test
    void unsuccessfulRequest_invalidDateFormat() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        String url = "http://localhost:%d/firstCommonHoliday?date=%s&countryCode1=%s&countryCode2=%s".formatted(port, "1.1.2025", "PL", "DE");
        ResponseEntity<ProblemDetail> forObject = restTemplate.getForEntity(url, ProblemDetail.class);
        Assertions.assertEquals(HttpStatusCode.valueOf(400), forObject.getStatusCode());
    }

    @Test
    void unsuccessfulRequest_missingCountry2Param() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        String url = "http://localhost:%d/firstCommonHoliday?date=%s&countryCode1=%s".formatted(port, "2025-01-01", "PL");
        ResponseEntity<ProblemDetail> forObject = restTemplate.getForEntity(url, ProblemDetail.class);
        Assertions.assertEquals(HttpStatusCode.valueOf(400), forObject.getStatusCode());
    }

    @Test
    void unsuccessfulRequest_invalidPath() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        String url = "http://localhost:%d/invalidPath?date=%s&countryCode1=%s".formatted(port, "2025-01-01", "PL");
        ResponseEntity<Void> forObject = restTemplate.getForEntity(url, Void.class);
        Assertions.assertEquals(HttpStatusCode.valueOf(404), forObject.getStatusCode());
    }
}


