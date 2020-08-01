package nl.ordina.bertkoor.camelcase;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.test.context.support.TestPropertySourceUtils;

public class WireMockInitializer {

    public static WireMockServer newStartedServer() {
        WireMockServer server = new WireMockServer(WireMockConfiguration.options().port(0));
        server.start();
        return server;
    }

}
