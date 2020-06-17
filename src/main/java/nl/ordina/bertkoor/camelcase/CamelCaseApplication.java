package nl.ordina.bertkoor.camelcase;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;

@SpringBootApplication
public class CamelCaseApplication extends RouteBuilder {

	public static void main(String[] args) {
		SpringApplication.run(CamelCaseApplication.class, args);
	}

	@Override
	public void configure() {
		restConfiguration()	// connect the Apache Camel Rest component to the Spring Boot servlet
				.component("servlet");

		rest().get("/ping") // be aware this is within the servlet's context path set in application.properties
				.route().routeId("ping")
				.setHeader(Exchange.CONTENT_TYPE, constant(MediaType.TEXT_PLAIN_VALUE))
				.setBody(constant("pong"));

		rest().get("/camel/{id}/internal")
				.route().routeId("id-internal")
				.to(TransformIdBean.URI)
				.setHeader(Exchange.CONTENT_TYPE, constant(MediaType.TEXT_PLAIN_VALUE))
				.setBody(simple("${header.id}"));
	}

}
