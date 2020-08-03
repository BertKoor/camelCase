package nl.ordina.bertkoor.camelcase;

import nl.ordina.bertkoor.camelcase.repo.CamelRegistryBean;
import nl.ordina.bertkoor.camelcase.beans.TransformIdBean;
import nl.ordina.bertkoor.camelcase.repo.CamelRegistry;
import nl.ordina.bertkoor.camelcase.model.RegistryData;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = CamelRegistry.class)
@EntityScan(basePackageClasses = RegistryData.class)
public class CamelCaseApplication extends RouteBuilder {

    public static final String ERROR_URI = "direct:error";

    public static void main(String[] args) {
        SpringApplication.run(CamelCaseApplication.class, args);
    }

    @Override
    public void configure() {
        restConfiguration()    // connect the Apache Camel Rest component to the Spring Boot servlet
                .component("servlet");

        configureErrorHandling();
        configureEndpointPing();
        configureEndpointIdInternal();
        configureEndpointRegistry();
    }

    private void configureErrorHandling() {
        onException(IllegalArgumentException.class)
                .routeId("badRequest")
                .handled(true)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpStatus.BAD_REQUEST.value()))
                .to(ERROR_URI);

        from(ERROR_URI) // other routes can continue here
                .setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON_VALUE))
                .transform().simple("{\"message\":\"${exception.message}\"}");
    }

    private void configureEndpointPing() {
        rest().get("/ping") // be aware this is within the servlet's context path set in application.properties
                .route().routeId("ping")
                .setHeader(Exchange.CONTENT_TYPE, constant(MediaType.TEXT_PLAIN_VALUE))
                .setBody(constant("pong"));
    }

    private void configureEndpointIdInternal() {
        rest().get("/camel/{id}/internal")
                .route().routeId("id-internal")
                .to(TransformIdBean.URI)
                .setHeader(Exchange.CONTENT_TYPE, constant(MediaType.TEXT_PLAIN_VALUE))
                .setBody(simple("${header.id}"));
    }

    private void configureEndpointRegistry() {
        rest().get("/camel/{id}/registry")
                .route().routeId("id-registry")
                .to(TransformIdBean.URI)
                .to(CamelRegistryBean.URI)
                .marshal().json();
    }
}
