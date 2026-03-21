package ru.urfu.lrviz.api.rest.docs;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.http.HttpHeaders.ACCEPT;
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
import static ru.urfu.lrviz.core.GrammarDtoExamples.getAsBuildRequestBodyFor;
import static ru.urfu.lrviz.core.GrammarExamples.G_8;

/**
 *
 * @author fenya
 * @since 15.03.2026
 */
class RenderLALR1DocsTest extends AbstractMethodDocsTest {
    private static final String DOCUMENTED_PATH = "/api/{version}/build/lalr1";

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
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getAsBuildRequestBodyFor(G_8)))
                .andExpect(status().isOk())
                .andDo(MvcResult::getAsyncResult)
                .andDo(MockMvcRestDocumentation.document(getSnippetPath(),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(),
                        pathParameters(VERSION_PARAMETER),
                        queryParameters(SIZE_PARAMETER),
                        requestHeaders(CONTENT_TYPE_PNG_HEADER),
                        BUILD_LALR_AUTOMATON_REQUEST,
                        responseHeaders(CONTENT_TYPE_JSON_HEADER),
                        responseImagePng("renderLalr1")));
    }
}
