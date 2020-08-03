package nl.ordina.bertkoor.camelcase.mocks;

import com.github.tomakehurst.wiremock.WireMockServer;
import nl.ordina.bertkoor.camelcase.test.MyCamelSpringTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public abstract class AbstractMockTest {

    private static WireMockServer mockServer;

    @BeforeAll
    static void setupMockServer() {
        mockServer = MyCamelSpringTest.WireMockInitializer.newStartedServer();
    }

    @AfterAll
    static void tearDownMockServer() {
        mockServer.stop();
    }

    public int wiremockPort() {
        return mockServer.port();
    }

}
