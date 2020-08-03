package nl.ordina.bertkoor.camelcase.test;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import nl.ordina.bertkoor.camelcase.mocks.CcrsResponseTransformer;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestHelper;
import org.apache.camel.test.spring.junit5.CamelSpringTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.TestPropertySourceUtils;

/**
 * Abstraction for unit testing a piece of Camel route without starting the whole Spring Boot app.
 *
 * Hacked from examples:
 * https://camel.apache.org/manual/latest/testing.html#Testing-SpringTestwithJavaConfigExample
 * https://camel.apache.org/components/latest/others/test-spring-junit5.html#_using_the_camelspringtest_annotation
 *
 * Testing a piece of a Camel route with Spring context NOT loaded from a XML file is not popular.
 * https://camel.465427.n5.nabble.com/Question-for-CamelSpringDelegatingTestContextLoader-Java-Config-td5821843.html
 *
 * In theory the CamelSpringDelegatingTestContextLoader seems not needed,
 * but in practice I cannot get that working.
 * The ProducerTemplate and MockEndpoint remain null.
 */
@CamelSpringTest
@TestPropertySource(locations = "classpath:application.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class MyCamelSpringTest {

    /**
     * Dirty hack: avoid NPE in CamelSpringDelegatingTestContextLoader.
     * Maybe that's why it is deprecated ;-)
     */
    static {
        CamelSpringTestHelper.setTestClass(Object.class);
    }

    public static final String DIRECT_START_URI = "direct:start";
    public static final String MOCK_RESULT_URI = "mock:result";

    @Produce(DIRECT_START_URI)
    public ProducerTemplate producer;

    @EndpointInject(MOCK_RESULT_URI)
    public MockEndpoint resultEndpoint;

    public static class WireMockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, ApplicationListener {

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
}
