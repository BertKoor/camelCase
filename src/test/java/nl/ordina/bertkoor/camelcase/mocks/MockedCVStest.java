package nl.ordina.bertkoor.camelcase.mocks;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * Test functionality of Wiremock implementation of the CVS: Camel Value Service.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MockedCVStest {

    private static final String cvsEndpoint = "/mock-api/cvs/value";

    private WireMockServer mockServer = new WireMockServer(WireMockConfiguration.options().port(0));
    private int wiremockPort;

    @BeforeAll
    void setup() {
        mockServer.start();
        wiremockPort = mockServer.port();
    }

    @AfterAll
    void tearDown() {
        mockServer.stop();
    }

    @Test
    void camelValue_ok() {
        RestAssured.given().port(wiremockPort)
                .body("{\"weight\" : 2, \"age\" : 2, \"humps\" : 2, \"condition\" : 2, \"rating\" : 2}")
                .when().post(cvsEndpoint)
                .then().statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .body("value", is(42));
    }

    @Test
    void camelValue_badRequest_missingWeight() {
        RestAssured.given().port(wiremockPort)
                .body("{\"age\" : 2, \"humps\" : 2, \"condition\" : 2, \"rating\" : 2}")
                .when().post(cvsEndpoint)
                .then().statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.TEXT)
                .content(equalTo("Bad request"));
    }

    @Test
    void camelValue_badRequest_missingAge() {
        RestAssured.given().port(wiremockPort)
                .body("{\"weight\" : 2, \"humps\" : 2, \"condition\" : 2, \"rating\" : 2}")
                .when().post(cvsEndpoint)
                .then().statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.TEXT)
                .content(equalTo("Bad request"));
    }

    @Test
    void camelValue_badRequest_missingHumps() {
        RestAssured.given().port(wiremockPort)
                .body("{\"weight\" : 2, \"age\" : 2, \"condition\" : 2, \"rating\" : 2}")
                .when().post(cvsEndpoint)
                .then().statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.TEXT)
                .content(equalTo("Bad request"));
    }

    @Test
    void camelValue_badRequest_missingCondition() {
        RestAssured.given().port(wiremockPort)
                .body("{\"weight\" : 2, \"age\" : 2, \"humps\" : 2, \"rating\" : 2}")
                .when().post(cvsEndpoint)
                .then().statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.TEXT)
                .content(equalTo("Bad request"));
    }

    @Test
    void camelValue_badRequest_missingRating() {
        RestAssured.given().port(wiremockPort)
                .body("{\"weight\" : 2, \"age\" : 2, \"humps\" : 2, \"condition\" : 2}")
                .when().post(cvsEndpoint)
                .then().statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.TEXT)
                .content(equalTo("Bad request"));
    }

}
