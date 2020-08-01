package nl.ordina.bertkoor.camelcase.mocks;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * Test functionality of Wiremock implementation of the CHS: Camel Health Service.
 */
public class MockedCHStest extends AbstractMockTest {

    private static final String chsEndpoint = "/mock-api/chs/physical_health_status/";

    @Test
    void camelPhs_badRequest() {
        RestAssured.given().port(this.wiremockPort())
                .when().get(chsEndpoint)
                .then().statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.TEXT)
                .content(equalTo("Bad request"));
    }

    @Test
    void camelPhs_9_badRequest() {
        RestAssured.given().port(this.wiremockPort())
                .when().get(chsEndpoint + "9")
                .then().statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.TEXT)
                .content(equalTo("Bad request"));
    }

    @Test
    void camelPhs_10_ok() {
        RestAssured.given().port(this.wiremockPort())
                .when().get(chsEndpoint + "10")
                .then().statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .body("phs", is(7));
    }

    @Test
    void camelPhs_10A_badRequest() {
        RestAssured.given().port(this.wiremockPort())
                .when().get(chsEndpoint + "10A")
                .then().statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.TEXT)
                .content(equalTo("Bad request"));
    }

    @Test
    void camelPhs_9_999_999_999_ok() {
        RestAssured.given().port(this.wiremockPort())
                .when().get(chsEndpoint + "9999999999")
                .then().statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .body("phs", is(7));
    }

    @Test
    void camelPhs_10_000_000_000_badRequest() {
        RestAssured.given().port(this.wiremockPort())
                .when().get(chsEndpoint + "10000000000")
                .then().statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.TEXT)
                .content(equalTo("Bad request"));
    }

}
