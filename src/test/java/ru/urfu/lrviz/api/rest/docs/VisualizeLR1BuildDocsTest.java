package ru.urfu.lrviz.api.rest.docs;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.urfu.lrviz.GrammarDtoExamples.getAsBuildRequestBodyFor;
import static ru.urfu.lrviz.api.VersionsConstants.V1;
import static ru.urfu.lrviz.api.rest.docs.snippets.DocumentationConstants.*;
import static ru.urfu.lrviz.core.GrammarExamples.G_8;

/**
 *
 * @author fenya
 * @since 05.04.2026
 */
class VisualizeLR1BuildDocsTest extends AbstractMethodDocsTest {

    private static final String DOCUMENTED_PATH = "/api/{version}/build/lr1";

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
        return APPLICATION_OCTET_STREAM_VALUE;
    }

    @Test
    void document() throws Exception {
        this.mockMvc.perform(post(DOCUMENTED_PATH, V1)
                        .header(ACCEPT, APPLICATION_OCTET_STREAM_VALUE)
                        .contentType(APPLICATION_JSON)
                        .content(getAsBuildRequestBodyFor(G_8)))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document(getSnippetPath(), preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        requestHeaders(ACCEPT_ZIP_HEADER),
                        pathParameters(VERSION_PARAMETER),
                        BUILD_LR_AUTOMATON_REQUEST.andWithPrefix(VISUALIZE_OPERATIONS_FIELD_NAME,
                                VISUALIZE_OPTION_VISUALIZE_OPERATIONS,
                                VISUALIZE_OPTION_COLORIZE_TRANSITIONS),
                        responseHeaders(CONTENT_TYPE_ZIP_HEADER)));
    }
}
