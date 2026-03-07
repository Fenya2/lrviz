package ru.urfu.lrviz.api.rest.docs;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.urfu.lrviz.api.VersionsConstants.V1;
import static ru.urfu.lrviz.api.rest.docs.snippets.DocumentationConstants.*;
import static ru.urfu.lrviz.api.rest.docs.snippets.ImageSnippet.responseImagePng;
import static ru.urfu.lrviz.core.GrammarExamples.G_6;
import static ru.urfu.lrviz.core.GrammarExamples.getAsJson;

/**
 * @author fenya
 * @since 05.03.2026
 */
class RenderLR0DocsTest extends AbstractMethodDocsTest {
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
        return IMAGE_PNG_VALUE;
    }

    @Test
    void document() throws Exception {
        this.mockMvc.perform(post(DOCUMENTED_PATH, V1)
                        .header(ACCEPT, IMAGE_PNG_VALUE)
                        .contentType(APPLICATION_JSON)
                        .content(getAsJson(G_6)))
                .andExpect(status().isOk())
                .andDo(MvcResult::getAsyncResult)
                .andDo(MockMvcRestDocumentation.document(getSnippetPath(),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(),
                        pathParameters(VERSION_PARAMETER),
                        queryParameters(SIZE_PARAMETER),
                        requestHeaders(ACCEPT_PNG_HEADER),
                        BUILD_LR_AUTOMATON_REQUEST,
                        responseHeaders(CONTENT_TYPE_PNG_HEADER),
                        responseImagePng("renderLr0")));
    }
}
