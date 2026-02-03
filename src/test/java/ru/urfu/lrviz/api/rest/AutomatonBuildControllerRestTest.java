package ru.urfu.lrviz.api.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;
import ru.urfu.lrviz.api.dto.GrammarDto;
import ru.urfu.lrviz.api.dto.RuleDto;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AutomatonBuildControllerRestTest {

    @LocalServerPort
    private int port;

    private RestTestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
    }

    /**
     * D => T L;
     * T => int | real;
     * L => L : a | a;
     */
    @Test
    void buildLR0_shouldReturnResult() throws Exception {
        GrammarDto grammarDto = new GrammarDto(
                List.of("i", "r", "a", ";"),
                List.of("D", "T", "L"),
                List.of(
                        new RuleDto("D", "TL"),
                        new RuleDto("T", "i"),
                        new RuleDto("T", "r"),
                        new RuleDto("L", "L;a"),
                        new RuleDto("L", "a")
                ),
                "D");

        restClient.post()
                .uri("/build/lr0")
                .contentType(MediaType.APPLICATION_JSON)
                .body(grammarDto)
                .exchange()
                .expectStatus().isOk();
    }
}
