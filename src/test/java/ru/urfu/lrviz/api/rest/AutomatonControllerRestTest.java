package ru.urfu.lrviz.api.rest;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.EntityExchangeResult;
import ru.urfu.lrviz.api.dto.BuildOptionsDto;
import ru.urfu.lrviz.api.dto.LRBuildRequestDto;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.IMAGE_PNG;
import static ru.urfu.lrviz.api.VersionsConstants.V1;
import static ru.urfu.lrviz.api.dto.convert.BuildOptionsDtoConverter.END_TO_END_NUMERIC_STRATEGY_CODE;
import static ru.urfu.lrviz.core.GrammarDtoExamples.getAsDto;
import static ru.urfu.lrviz.core.GrammarDtoExamples.getAsBuildRequestBodyFor;
import static ru.urfu.lrviz.core.GrammarExamples.G_1;

class AutomatonControllerRestTest extends AbstractRestTest {
    private static final int IMAGE_SIZE = 250;
    private static final String BUILD_SEGMENT = "/build";

    @Test
    void buildLR0() {
        getRestClient().post()
                .uri(BUILD_SEGMENT + "/lr0")
                .apiVersion(V1)
                .accept(APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(getAsBuildRequestBodyFor(G_1))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON);
    }

    @Test
    void renderLR0Png() {
        getRestClient().post()
                .uri(BUILD_SEGMENT + "/lr0?size=" + IMAGE_SIZE)
                .apiVersion(V1)
                .accept(IMAGE_PNG)
                .contentType(MediaType.APPLICATION_JSON)
                .body(getAsBuildRequestBodyFor(G_1))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(IMAGE_PNG)
                .expectBody().consumeWith(AutomatonControllerRestTest::isPngSignature);
    }

    @Test
    void buildWithEndToEndNamingStrategy() {
        getRestClient().post()
                .uri(BUILD_SEGMENT + "/lr0")
                .apiVersion(V1)
                .accept(APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new LRBuildRequestDto(getAsDto(G_1), new BuildOptionsDto(END_TO_END_NUMERIC_STRATEGY_CODE, null)))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON);
    }

    @Test
    void buildLR1() {
        getRestClient().post()
                .uri(BUILD_SEGMENT + "/lr1")
                .apiVersion(V1)
                .accept(APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(getAsBuildRequestBodyFor(G_1))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON);
    }

    @Test
    void renderLR1Png() {
        getRestClient().post()
                .uri(BUILD_SEGMENT + "/lr1?size=" + IMAGE_SIZE)
                .apiVersion(V1)
                .accept(IMAGE_PNG)
                .contentType(APPLICATION_JSON)
                .body(getAsBuildRequestBodyFor(G_1))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(IMAGE_PNG)
                .expectBody().consumeWith(AutomatonControllerRestTest::isPngSignature);
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
