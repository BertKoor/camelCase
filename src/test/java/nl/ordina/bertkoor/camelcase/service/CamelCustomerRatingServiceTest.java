package nl.ordina.bertkoor.camelcase.service;

import nl.ordina.bertkoor.camelcase.model.CCRSResponse;
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
public class CamelCustomerRatingServiceTest extends MyCamelSpringTest {

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private CamelCustomerRatingServiceAdapter ccrsAdapter;

    @Autowired
    private CamelCustomerRatingServiceClient ccrsClient;

    @TestConfiguration
    @ComponentScan(resourcePattern = "CamelCustomerRatingService*.class")
    public static class CamelSpringTestConfig extends SingleRouteCamelConfiguration {
        @Override
        public RouteBuilder route() {
            return new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from(DIRECT_START_URI)
                            .to(CamelCustomerRatingServiceAdapter.URI)
                            .to(MOCK_RESULT_URI);
                }
            };
        }
    }

    @BeforeEach
    public void setUp() throws Exception {
        // Hack our RouteBuilder into the context. Bean definition / ComponentScan doesn't work for RouteBuilders.
        if (camelContext.getRoute(CamelCustomerRatingServiceAdapter.URI) == null) {
            camelContext.addRoutes(ccrsAdapter);
        }
        if (camelContext.getRoute(CamelCustomerRatingServiceClient.URI) == null) {
            camelContext.addRoutes(ccrsClient);
        }
    }

    @Test
    void test_noRating() throws InterruptedException {
        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedBodiesReceived(new CCRSResponse(null));

        producer.sendBodyAndHeader(null, "id", "100");

        resultEndpoint.assertIsSatisfied();
    }

    @Test
    void test_rating3() throws InterruptedException {
        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedBodiesReceived(new CCRSResponse(3));

        producer.sendBodyAndHeader(null, "id", "12345");

        resultEndpoint.assertIsSatisfied();
    }
}
