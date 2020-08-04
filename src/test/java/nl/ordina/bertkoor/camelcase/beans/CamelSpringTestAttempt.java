package nl.ordina.bertkoor.camelcase.beans;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.spring.javaconfig.SingleRouteCamelConfiguration;
import org.apache.camel.test.spring.CamelSpringDelegatingTestContextLoader;
import org.apache.camel.test.spring.CamelSpringTestHelper;
import org.apache.camel.test.spring.junit5.CamelSpringTest;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

/**
 * Example based on the second piece of code at https://camel.apache.org/manual/latest/testing.html#Testing-SpringTestwithJavaConfigExample
 * Both the producer and resultEndpoint are null when the test starts.
 * That can be fixed with @ContextConfiguration(loader = CamelSpringDelegatingTestContextLoader.class)
 * but then there's a NPE at org.apache.camel.test.spring.CamelAnnotationsHandler.handleDisableJmx(CamelAnnotationsHandler.java:90)
 * which gets fixed by adding a static method that sets a dummy test class in CamelSpringTestHelper.
 */
@CamelSpringTest @MockEndpoints
@ContextConfiguration(
        classes = CamelSpringTestAttempt.TestConfig.class
        // , loader = CamelSpringDelegatingTestContextLoader.class
)
public class CamelSpringTestAttempt {

//    static { CamelSpringTestHelper.setTestClass(Object.class); }

    static final String DIRECT_START_URI = "direct:start";
    static final String MOCK_RESULT_URI = "mock:direct:result";

    @Produce(DIRECT_START_URI)
    ProducerTemplate producer;

    @EndpointInject(MOCK_RESULT_URI)
    MockEndpoint resultEndpoint;

    @Configuration @ComponentScan(basePackageClasses = TransformIdBean.class)
    public static class TestConfig extends SingleRouteCamelConfiguration {
        @Bean @Override
        public RouteBuilder route() {
            return new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from(DIRECT_START_URI)
                            .log("before: header.id = ${header.id}")
                            .to(TransformIdBean.URI)
                            .log("after : header.id = ${header.id}")
                            .to(MOCK_RESULT_URI);
                }
            };
        }
    }

    @Test
    public void testRoute() throws InterruptedException {
        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedHeaderReceived("id", "10");

        producer.sendBodyAndHeader(null, "id", "00000A");

        resultEndpoint.assertIsSatisfied();
    }
}
