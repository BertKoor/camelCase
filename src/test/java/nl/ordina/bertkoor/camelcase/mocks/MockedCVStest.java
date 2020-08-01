package nl.ordina.bertkoor.camelcase.mocks;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * Test functionality of Wiremock implementation of the CVS: Camel Value Service.
 */
public class MockedCVStest extends AbstractMockTest {

    private static final String cvsEndpoint = "/mock-api/cvs/value";

    @Test
    void camelValue_ok() {
        RestAssured.given().port(this.wiremockPort())
                .body("{\"weight\" : 2, \"age\" : 2, \"humps\" : 2, \"condition\" : 2, \"rating\" : 2}")
                .when().post(cvsEndpoint)
                .then().statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .body("value", is(42));
    }

    @Test
    void camelValue_badRequest_missingWeight() {
        RestAssured.given().port(this.wiremockPort())
                .body("{\"age\" : 2, \"humps\" : 2, \"condition\" : 2, \"rating\" : 2}")
                .when().post(cvsEndpoint)
                .then().statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.TEXT)
                .content(equalTo("Bad request"));
    }

    @Test
    void camelValue_badRequest_missingAge() {
        RestAssured.given().port(this.wiremockPort())
                .body("{\"weight\" : 2, \"humps\" : 2, \"condition\" : 2, \"rating\" : 2}")
                .when().post(cvsEndpoint)
                .then().statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.TEXT)
                .content(equalTo("Bad request"));
    }

    @Test
    void camelValue_badRequest_missingHumps() {
        RestAssured.given().port(this.wiremockPort())
                .body("{\"weight\" : 2, \"age\" : 2, \"condition\" : 2, \"rating\" : 2}")
                .when().post(cvsEndpoint)
                .then().statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.TEXT)
                .content(equalTo("Bad request"));
    }

    @Test
    void camelValue_badRequest_missingCondition() {
        RestAssured.given().port(this.wiremockPort())
                .body("{\"weight\" : 2, \"age\" : 2, \"humps\" : 2, \"rating\" : 2}")
                .when().post(cvsEndpoint)
                .then().statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.TEXT)
                .content(equalTo("Bad request"));
    }

    @Test
    void camelValue_ok_missingRating() {
        RestAssured.given().port(this.wiremockPort())
                .body("{\"weight\" : 2, \"age\" : 2, \"humps\" : 2, \"condition\" : 2}")
                .when().post(cvsEndpoint)
                .then().statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .body("value", is(42));
    }

}
