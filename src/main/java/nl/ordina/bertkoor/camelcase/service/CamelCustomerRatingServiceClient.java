package nl.ordina.bertkoor.camelcase.service;

import nl.ordina.bertkoor.camelcase.model.CCRSResponse;
import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CamelCustomerRatingServiceClient extends RouteBuilder {

    public static final String URI = "direct:ccrs_call";

    @Value("${app.ccrs.host}")
    private String host;

    @Value("${app.ccrs.endpoint}")
    private String endpoint;

    @Override
    public void configure() {
        Predicate isHttpStatus204 = this.header(Exchange.HTTP_RESPONSE_CODE).isEqualTo(HttpStatus.SC_NO_CONTENT);

        from(URI)
                .routeId(URI)
                .toD("http:" + host + endpoint + "${header.id}")
                .choice()
                    .when(isHttpStatus204).setBody(constant("{}"))
                .endChoice();
                // The route should continue with unmarshalling json
                // but after the choice it is no longer a RouteDefinition.
                // So the ServiceAdapter takes care of the unmarshalling.
    }

}
