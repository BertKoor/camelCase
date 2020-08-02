package nl.ordina.bertkoor.camelcase.service;

import nl.ordina.bertkoor.camelcase.model.CHSResponse;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CamelHealthServiceClient extends RouteBuilder {

    public static final String URI = "direct:chs";

    @Value("${app.chs.host}")
    private String host;

    @Value("${app.chs.endpoint}")
    private String endpoint;

    @Override
    public void configure() {
        from(URI)
                .routeId(URI)
                // toD = 'Dynamic' form of 'to', which evaluates expressions
                .toD("http:" + host + endpoint + "${header.id}")
                .unmarshal().json(JsonLibrary.Jackson, CHSResponse.class);
    }
}
