package nl.ordina.bertkoor.camelcase.service;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpMethods;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//@Component
public class CamelValueServiceClient extends RouteBuilder {

    public static final String URI = "direct:cvs";

    @Value("${app.cvs.host}")
    private String host;

    @Value("${app.cvs.endpoint}")
    private String endpoint;

    @Override
    public void configure() {
        from(URI)
                .routeId(URI)
                .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.POST))
                .setHeader(Exchange.HTTP_URI, simple(host + endpoint))
                .to("http:xxx");
    }
}
