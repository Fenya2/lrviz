package ru.urfu.lrviz.api.rest.docs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.urfu.lrviz.api.docs.OpenApiConfig.DETAILED_API_DOCS_PATH;

/**
 * @author fenya
 * @since 19.04.2026
 */
@SpringBootTest
class DocsRedirectTest {
    MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void redirectFromApiDocs() throws Exception {
        mockMvc.perform(get("/api/docs"))
                .andExpect(status().isPermanentRedirect())
                .andExpect(header().string(HttpHeaders.LOCATION, DETAILED_API_DOCS_PATH));
        mockMvc.perform(get("/api/docs/"))
                .andExpect(status().isPermanentRedirect())
                .andExpect(header().string(HttpHeaders.LOCATION, DETAILED_API_DOCS_PATH));
    }
}