package ru.urfu.lrviz.api.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.EntityExchangeResult;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.client.ApiVersionInserter;
import ru.urfu.lrviz.api.dto.BuildOptionsDto;
import ru.urfu.lrviz.api.dto.LRBuildRequestDto;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.IMAGE_PNG;
import static ru.urfu.lrviz.api.VersionsConstants.V1;
import static ru.urfu.lrviz.api.dto.convert.BuildOptionsDtoConverter.END_TO_END_NUMERIC_STRATEGY_CODE;
import static ru.urfu.lrviz.core.GrammarExamples.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AutomatonBuildControllerRestTest {
    private static final int IMAGE_SIZE = 250;

    @LocalServerPort
    private int port;

    private RestTestClient restClient;

    @BeforeEach
    void setUp() {
        String baseUrl = "http://localhost:" + port + "/api/build";
        restClient = RestTestClient
                .bindToServer()
                .apiVersionInserter(ApiVersionInserter.usePathSegment(1))
                .baseUrl(baseUrl)
                .build();
    }

    @Test
    void buildLR0() {
        restClient.post()
                .uri("/lr0")
                .apiVersion(V1)
                .accept(APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(getAsJson(G_1))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON);
    }

    @Test
    void renderLR0Png() {
        restClient.post()
                .uri("/lr0?size=" + IMAGE_SIZE)
                .apiVersion(V1)
                .accept(IMAGE_PNG)
                .contentType(MediaType.APPLICATION_JSON)
                .body(getAsJson(G_1))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(IMAGE_PNG)
                .expectBody().consumeWith(AutomatonBuildControllerRestTest::isPngSignature);
    }

    @Test
    void buildWithEndToEndNamingStrategy() {
        restClient.post()
                .uri("/lr0")
                .apiVersion(V1)
                .accept(APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new LRBuildRequestDto(getAsDto(G_1), new BuildOptionsDto(END_TO_END_NUMERIC_STRATEGY_CODE)))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON);
    }

    @Test
    void buildLR1() {
        restClient.post()
                .uri("/lr1")
                .apiVersion(V1)
                .accept(APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(getAsJson(G_1))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON);
    }

    @Test
    void renderLR1Png() {
        restClient.post()
                .uri("/lr1?size=" + IMAGE_SIZE)
                .apiVersion(V1)
                .accept(IMAGE_PNG)
                .contentType(APPLICATION_JSON)
                .body(getAsJson(G_1))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(IMAGE_PNG)
                .expectBody().consumeWith(AutomatonBuildControllerRestTest::isPngSignature);
    }

    private static void isPngSignature(EntityExchangeResult<byte[]> result) {
        byte[] bytes = result.getResponseBody();
        assert bytes != null;
        assert bytes.length > 8;
        assert bytes[0] == (byte) 0x89;
        assert bytes[1] == 0x50;
        assert bytes[2] == 0x4E;
        assert bytes[3] == 0x47;
    }
}
