package ru.urfu.lrviz.api.rest;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.client.ApiVersionInserter;

/**
 *
 * @author fenya
 * @since 22.03.2026
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractRestTest {
    @LocalServerPort
    private int port;

    private RestTestClient restClient;

    @BeforeEach
    void setUp() {
        String baseUrl = "http://localhost:" + port + "/api";
        restClient = RestTestClient
                .bindToServer()
                .apiVersionInserter(ApiVersionInserter.usePathSegment(1))
                .baseUrl(baseUrl)
                .build();
    }

    protected RestTestClient getRestClient() {
        return restClient;
    }
}
