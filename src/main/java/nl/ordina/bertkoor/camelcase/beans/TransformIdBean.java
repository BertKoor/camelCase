package nl.ordina.bertkoor.camelcase.beans;

import nl.ordina.bertkoor.camelcase.logic.LicenseNumberTransformer;
import org.apache.camel.Consume;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component
public class TransformIdBean {
    public static final String URI = "direct:transform_id";

    @Consume(URI)
    public void process(Exchange exchange) {
        String licenseId = (String) exchange.getIn().getHeader("id");
        Long licenseNumber = LicenseNumberTransformer.transform(licenseId);
        exchange.getIn().setHeader("id", licenseNumber);
    }
}
