package nl.ordina.bertkoor.camelcase.beans;

import nl.ordina.bertkoor.camelcase.test.MyCamelSpringTest;
import nl.ordina.bertkoor.camelcase.test.MyCamelSpringTestContextLoader;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.javaconfig.SingleRouteCamelConfiguration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration (loader = MyCamelSpringTestContextLoader.class)
public class TransformIdBeanTest extends MyCamelSpringTest {

    @TestConfiguration
    @ComponentScan(basePackageClasses = TransformIdBean.class)
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
