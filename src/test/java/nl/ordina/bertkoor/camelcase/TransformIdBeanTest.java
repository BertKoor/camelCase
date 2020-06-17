package nl.ordina.bertkoor.camelcase;

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
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

/**
 * Example of unit testing a piece of Camel route without starting the whole Spring Boot app.
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
@ContextConfiguration(classes = TransformIdBeanTest.CamelSpringTestConfig.class, loader = CamelSpringDelegatingTestContextLoader.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TransformIdBeanTest {

    /**
     * Dirty hack: avoid NPE in CamelSpringDelegatingTestContextLoader.
     */
    @BeforeAll static void setup() {
        CamelSpringTestHelper.setTestClass(TransformIdBeanTest.class);
    }

    private static final String DIRECT_START_URI = "direct:start";
    private static final String MOCK_RESULT_URI = "mock:result";

    @Produce(DIRECT_START_URI)
    ProducerTemplate producer;

    @EndpointInject(MOCK_RESULT_URI)
    MockEndpoint resultEndpoint;

    @TestConfiguration @ComponentScan(basePackageClasses = TransformIdBean.class)
    public static class CamelSpringTestConfig extends SingleRouteCamelConfiguration {
        @Bean public RouteBuilder route() {
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
    void test_ok() throws InterruptedException {
        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedHeaderReceived("id", "10");

        producer.sendBodyAndHeader(null, "id", "00000A");

        resultEndpoint.assertIsSatisfied();
    }
}
