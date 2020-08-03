package nl.ordina.bertkoor.camelcase.service;

import nl.ordina.bertkoor.camelcase.model.CHSResponse;
import nl.ordina.bertkoor.camelcase.test.MyCamelSpringTest;
import nl.ordina.bertkoor.camelcase.test.MyCamelSpringTestContextLoader;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.javaconfig.SingleRouteCamelConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(loader = MyCamelSpringTestContextLoader.class,
        initializers = MyCamelSpringTest.WireMockInitializer.class)
public class CamelHealthServiceClientTest extends MyCamelSpringTest {
    private static final String DIRECT_START_URI = "direct:start";
    private static final String MOCK_RESULT_URI = "mock:result";

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private CamelHealthServiceClient chsClient;

    @TestConfiguration
    @ComponentScan(resourcePattern = "CamelHealthServiceClient.class")
    public static class CamelSpringTestConfig extends SingleRouteCamelConfiguration {
        @Override
        public RouteBuilder route() {
            return new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from(DIRECT_START_URI)
                            .to(CamelHealthServiceClient.URI)
                            .to(MOCK_RESULT_URI);
                }
            };
        }
    }

    @BeforeEach
    public void setUp() throws Exception {
        // Hack our RouteBuilder into the context. Bean definition / ComponentScan doesn't work for RouteBuilders.
        if (camelContext.getRoute(CamelHealthServiceClient.URI) == null) {
            camelContext.addRoutes(chsClient);
        }
    }

    @Test
    void test_ok() throws InterruptedException {
        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedBodiesReceived(new CHSResponse(7));

        producer.sendBodyAndHeader(null, "id", "123");

        resultEndpoint.assertIsSatisfied();
    }

}
