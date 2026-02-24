package ru.urfu.lrviz.api.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

import static ru.urfu.lrviz.api.VersionsConstants.V1;
import static ru.urfu.lrviz.core.GrammarExamples.G_1;
import static ru.urfu.lrviz.core.GrammarExamples.getJsonDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AutomatonBuildControllerRestTest {

    @LocalServerPort
    private int port;

    private RestTestClient restClient;

    @BeforeEach
    void setUp() {
        String baseUrl = "http://localhost:" + port + "/api/{version}/build";
        restClient = RestTestClient
                .bindToServer()
                .baseUrl(baseUrl)
                .build();
    }

    @Test
    void buildLR0() {
        RestTestClient.ResponseSpec response = restClient.post()
                .uri("/lr0", V1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(getJsonDto(G_1))
                .exchange();
        response.expectStatus().isOk();
    }

    @Test
    void buildLR1() {
        RestTestClient.ResponseSpec response = restClient.post()
                .uri("/lr1", V1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(getJsonDto(G_1))
                .exchange();
        response.expectStatus().isOk();
    }
}
