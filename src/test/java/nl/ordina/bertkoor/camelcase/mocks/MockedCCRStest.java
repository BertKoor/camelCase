package nl.ordina.bertkoor.camelcase.mocks;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class MockedCCRStest extends AbstractMockTest {
    private static final String ccrsEndpoint = "/mock-api/ccrs/rating/";

    @Test
    void camelRating_badRequest() {
        RestAssured.given().port(this.wiremockPort())
                .when().get(ccrsEndpoint)
                .then().statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.TEXT)
                .content(equalTo("Bad request"));
    }

    @Test
    void camelRating_9_badRequest() {
        RestAssured.given().port(this.wiremockPort())
                .when().get(ccrsEndpoint + "9")
                .then().statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.TEXT)
                .content(equalTo("Bad request"));
    }

    @Test
    void camelRating_10_noContent() {
        RestAssured.given().port(this.wiremockPort())
                .when().get(ccrsEndpoint + "10")
                .then().statusCode(SC_NO_CONTENT);
    }

    @Test
    void camelRating_110_noContent() {
        RestAssured.given().port(this.wiremockPort())
                .when().get(ccrsEndpoint + "110")
                .then().statusCode(SC_NO_CONTENT);
    }

    @Test
    void camelRating_111_scores1() {
        RestAssured.given().port(this.wiremockPort())
                .when().get(ccrsEndpoint + "111")
                .then().statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .body("rating", is(1));
    }

    @Test
    void camelRating_555_scores5() {
        RestAssured.given().port(this.wiremockPort())
                .when().get(ccrsEndpoint + "555")
                .then().statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .body("rating", is(5));
    }

    @Test
    void camelRating_12345_scores3() {
        RestAssured.given().port(this.wiremockPort())
                .when().get(ccrsEndpoint + "12345")
                .then().statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .body("rating", is(3));
    }
}
