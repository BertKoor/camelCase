package nl.ordina.bertkoor.camelcase;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class CamelCaseApplicationTests {

	@LocalServerPort
	private int port;

	@Test
	void welcomePageLoads() {
		RestAssured.given().port(port)
				.when().get("/")
				.then().statusCode(HttpStatus.SC_OK)
				.contentType(ContentType.HTML)
				.body("html.body.h1", equalTo("camelCase"));
	}

	@Test
	void actuatorInfoEndpointLoads() {
		RestAssured.given().port(port)
				.when().get("/actuator/info")
				.then().statusCode(HttpStatus.SC_OK)
				.contentType(ContentType.JSON)
				.body("appName", equalTo("camelCase"))
				.body("'camel.name'", equalTo("camelCaseContext"));
	}

	@Test
	void actuatorHealthEndpointLoads() {
		RestAssured.given().port(port)
				.when().get("/actuator/health")
				.then().statusCode(HttpStatus.SC_OK)
				.contentType(ContentType.JSON)
				.body("status", equalTo("UP"))
				.body("components.camel.status", equalTo("UP"));
	}

	@Test
	void pingPongTest() {
		RestAssured.given().port(port)
				.when().get("/my-api/ping")
				.then().statusCode(HttpStatus.SC_OK)
				.contentType(ContentType.TEXT)
				.content(equalTo("pong"));
	}

}
