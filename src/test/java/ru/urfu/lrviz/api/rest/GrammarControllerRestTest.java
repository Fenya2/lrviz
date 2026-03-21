package ru.urfu.lrviz.api.rest;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static ru.urfu.lrviz.api.VersionsConstants.V1;
import static ru.urfu.lrviz.core.GrammarDtoExamples.getAsDto;
import static ru.urfu.lrviz.core.GrammarExamples.G_9;

/**
 * @author fenya
 * @since 22.03.2026
 */
class GrammarControllerRestTest extends AbstractRestTest {
    @Test
    void first() {

        getRestClient().post()
                .uri("/grammar/first")
                .apiVersion(V1)
                .accept(APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(getAsDto(G_9))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody(new ParameterizedTypeReference<Map<String, List<String>>>() {})
                .isEqualTo(Map.of(
                        "R", List.of("x", "*"),
                        "S", List.of("x", "*"),
                        "L", List.of("x", "*")
                ));
    }
}
