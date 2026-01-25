package ru.urfu.lrviz.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.urfu.lrviz.api.dto.GrammarDto;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LrAutomatonController.class)
class BuildLr0Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldBuildLR0Automaton() throws Exception {
        GrammarDto grammar = new GrammarDto(
                Collections.emptyList(),
                List.of("S"), Collections.emptyList(), "S");

        mockMvc.perform(post("/api/automaton/build")
                        .param("type", "lr0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(grammar)))
                .andExpect(status().isOk());
    }
}