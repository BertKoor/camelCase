package nl.ordina.bertkoor.camelcase.service;

import nl.ordina.bertkoor.camelcase.model.CCRSResponse;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class CamelCustomerRatingServiceAdapter extends RouteBuilder {
    public static final String URI = "direct:ccrs";

    @Override
    public void configure() throws Exception {
        from(URI)
                .routeId(URI)
                .to(CamelCustomerRatingServiceClient.URI)
                .unmarshal().json(JsonLibrary.Jackson, CCRSResponse.class);
    }
}
