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
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(documentationConfiguration(restDocumentation)).build();
    }

    /**
     * @return документируемый метод
     */
    protected abstract HttpMethod getDocumentedMethod();

    /**
     * @return документируемая версия
     */
    protected abstract String getDocumentedVersion();

    /**
     * @return документируемый путь
     */
    protected abstract String getDocumentedPath();

    /**
     * @return тип возвращаемого контента
     */
    protected abstract String getProduces();

    /**
     * Формирует путь к файлу сниппета по документируемому методу, пути и версии и возвращаемому типу
     *
     * @return путь, куда будет сохранен снипет
     */
    protected final String getSnippetPath() {
        return String.join(File.separator, getDocumentedMethod().name(), getDocumentedVersion(), getProduces(), getDocumentedPath());
    }
}
