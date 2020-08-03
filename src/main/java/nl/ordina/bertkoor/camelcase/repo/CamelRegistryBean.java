package nl.ordina.bertkoor.camelcase.repo;

import nl.ordina.bertkoor.camelcase.model.RegistryData;
import org.apache.camel.Consume;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CamelRegistryBean {
    public static final String URI = "direct:cr";

    @Autowired
    private CamelRegistry registry;

    @Consume(URI)
    public void process(Exchange exchange) {
        Long id = (Long) exchange.getIn().getHeader("id");
        RegistryData data = registry.findById(id).orElse(null);
        exchange.getIn().setBody(data);
    }
}
