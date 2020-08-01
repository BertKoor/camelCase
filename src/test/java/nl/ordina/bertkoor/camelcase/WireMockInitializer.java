package nl.ordina.bertkoor.camelcase;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import nl.ordina.bertkoor.camelcase.mocks.CcrsResponseTransformer;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.test.context.support.TestPropertySourceUtils;

public class WireMockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, ApplicationListener {

    private WireMockServer mockServer;

    public static WireMockServer newStartedServer() {
        WireMockServer server = new WireMockServer(
                WireMockConfiguration.options()
                        .port(0)
                        .extensions(CcrsResponseTransformer.class)
        );
        server.start();
        return server;
    }

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        mockServer = newStartedServer();
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                configurableApplicationContext, "wiremock.server.port=" + mockServer.port());
        configurableApplicationContext.addApplicationListener(this);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextClosedEvent) {
            mockServer.stop();
        }
    }
}
