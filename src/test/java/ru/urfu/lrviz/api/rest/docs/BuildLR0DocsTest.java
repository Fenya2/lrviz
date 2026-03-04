package ru.urfu.lrviz.api.rest.docs;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;

import java.util.stream.Stream;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.urfu.lrviz.api.VersionsConstants.V1;
import static ru.urfu.lrviz.api.rest.docs.snippets.DocumentationConstants.*;
import static ru.urfu.lrviz.core.GrammarExamples.*;

/**
 * @author fenya
 * @since 05.02.2026
 */
class BuildLR0DocsTest extends AbstractMethodDocsTest {
    private static final String DOCUMENTED_PATH = "/api/{version}/build/lr0";

    @Override
    protected HttpMethod getDocumentedMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected String getDocumentedVersion() {
        return V1;
    }

    @Override
    protected String getDocumentedPath() {
        return DOCUMENTED_PATH;
    }

    @Override
    protected String getProduces() {
        return APPLICATION_JSON_VALUE;
    }

    @Test
    void buildLr0() throws Exception {
        this.mockMvc.perform(post(DOCUMENTED_PATH, V1)
                        .header(ACCEPT, APPLICATION_JSON_VALUE)
                        .contentType(APPLICATION_JSON)
                        .content(getJsonDto(G_5)))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document(getSnippetPath(), preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        requestHeaders(ACCEPT_JSON_HEADER),
                        requestFields(GRAMMAR_DTO),
                        responseHeaders(CONTENT_TYPE_JSON_HEADER),
                        responseFields(Stream.concat(LR0_AUTOMATON_DTO.stream(), BUILD_LR0_LOG_DTO.stream()).toList())));
    }
}
