package ru.urfu.lrviz.api.rest.docs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;


/**
 * @author fenya
 * @since 04.02.2026
 */
@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public abstract class AbstractMethodDocsTest {

    MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    /**
     * @return документируемый метод
     */
    protected abstract HttpMethod getDocumentedMethod();

    /**
     * @return документируемый путь
     */
    protected abstract String getDocumentedPath();

    protected final String getSnippetPath() {
        return getDocumentedMethod().name() + File.separator + getDocumentedPath();
    }
}
