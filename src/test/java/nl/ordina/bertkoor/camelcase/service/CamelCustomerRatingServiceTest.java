package nl.ordina.bertkoor.camelcase.service;

import nl.ordina.bertkoor.camelcase.WireMockInitializer;
import nl.ordina.bertkoor.camelcase.model.CCRSResponse;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.spring.javaconfig.SingleRouteCamelConfiguration;
import org.apache.camel.test.spring.CamelSpringDelegatingTestContextLoader;
import org.apache.camel.test.spring.CamelSpringTestHelper;
import org.apache.camel.test.spring.junit5.CamelSpringTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@CamelSpringTest
@ContextConfiguration(classes = CamelCustomerRatingServiceTest.CamelSpringTestConfig.class,
        loader = CamelSpringDelegatingTestContextLoader.class,
        initializers = WireMockInitializer.class)
@TestPropertySource(locations = "classpath:application.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CamelCustomerRatingServiceTest {
    private static final String DIRECT_START_URI = "direct:start";
    private static final String MOCK_RESULT_URI = "mock:result";

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private CamelCustomerRatingServiceAdapter ccrsAdapter;

    @Autowired
    private CamelCustomerRatingServiceClient ccrsClient;

    @Produce(DIRECT_START_URI)
    ProducerTemplate producer;

    @EndpointInject(MOCK_RESULT_URI)
    MockEndpoint resultEndpoint;

    @BeforeAll
    static void setup() { // Dirty hack: avoid NPE in CamelSpringDelegatingTestContextLoader.
        CamelSpringTestHelper.setTestClass(CamelCustomerRatingServiceTest.class);
    }

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
