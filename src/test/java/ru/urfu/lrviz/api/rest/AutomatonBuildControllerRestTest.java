package ru.urfu.lrviz.api.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

import static ru.urfu.lrviz.core.GrammarExamples.G_1;
import static ru.urfu.lrviz.core.GrammarExamples.getJsonDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AutomatonBuildControllerRestTest {

    @LocalServerPort
    private int port;

    private RestTestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
    }

    @Test
    void buildLR0() {
        RestTestClient.ResponseSpec response = restClient.post()
                .uri("/api/build/lr0")
                .contentType(MediaType.APPLICATION_JSON)
                .body(getJsonDto(G_1))
                .exchange();
        response.expectStatus().isOk();
    }

    @Test
    void buildLR1() {
        RestTestClient.ResponseSpec response = restClient.post()
                .uri("/api/build/lr1")
                .contentType(MediaType.APPLICATION_JSON)
                .body(getJsonDto(G_1))
                .exchange();
        response.expectStatus().isOk();
    }
}
