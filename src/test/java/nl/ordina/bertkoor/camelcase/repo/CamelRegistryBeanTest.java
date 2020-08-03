package nl.ordina.bertkoor.camelcase.repo;

import nl.ordina.bertkoor.camelcase.model.RegistryData;
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
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@CamelSpringTest
@TestPropertySource(locations = "classpath:application.properties")
@ContextConfiguration(classes = CamelRegistryBeanTest.CamelSpringTestConfig.class, loader = CamelSpringDelegatingTestContextLoader.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CamelRegistryBeanTest {
    private static final String DIRECT_START_URI = "direct:start";
    private static final String MOCK_RESULT_URI = "mock:result";

    @Produce(DIRECT_START_URI)
    ProducerTemplate producer;

    @EndpointInject(MOCK_RESULT_URI)
    MockEndpoint resultEndpoint;

    /**
     * Dirty hack: avoid NPE in CamelSpringDelegatingTestContextLoader.
     */
    @BeforeAll
    static void setup() {
        CamelSpringTestHelper.setTestClass(CamelRegistryBeanTest.class);
    }

    @TestConfiguration
    @EnableAutoConfiguration
    @ComponentScan(basePackageClasses = {CamelRegistryBean.class})
    @EnableJpaRepositories(basePackageClasses = CamelRegistry.class)
    @EntityScan(basePackageClasses = RegistryData.class)
    public static class CamelSpringTestConfig extends SingleRouteCamelConfiguration {
        @Bean
        public RouteBuilder route() {
            return new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from(DIRECT_START_URI)
                            .log("before: header.id = ${header.id}")
                            .to(CamelRegistryBean.URI)
                            .log("after : body = ${body}")
                            .to(MOCK_RESULT_URI);
                }
            };
        }
    }

    @Test
    void test_ok() throws InterruptedException {
        RegistryData expectedData = new RegistryData();
        expectedData.setId(10l);
        expectedData.setBirthYear(1990);
        expectedData.setBirthMonth(1);
        expectedData.setHumps(2);
        expectedData.setWeight(2500);

        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedBodiesReceived(expectedData);

        producer.sendBodyAndHeader(null, "id", 10l);

        resultEndpoint.assertIsSatisfied();
    }

}
