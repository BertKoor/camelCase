package nl.ordina.bertkoor.camelcase.repo;

import nl.ordina.bertkoor.camelcase.model.RegistryData;
import nl.ordina.bertkoor.camelcase.test.MyCamelSpringTest;
import nl.ordina.bertkoor.camelcase.test.MyCamelSpringTestContextLoader;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.javaconfig.SingleRouteCamelConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(loader = MyCamelSpringTestContextLoader.class)
public class CamelRegistryBeanTest extends MyCamelSpringTest {

    @TestConfiguration
    @ComponentScan(basePackageClasses = {CamelRegistryBean.class})
    @EnableAutoConfiguration
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
