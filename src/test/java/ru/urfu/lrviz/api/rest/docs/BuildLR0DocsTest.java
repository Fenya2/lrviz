package ru.urfu.lrviz.api.rest.docs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import ru.urfu.lrviz.api.dto.GrammarDto;
import ru.urfu.lrviz.api.dto.RuleDto;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author fenya
 * @since 05.02.2026
 */
class BuildLR0DocsTest extends AbstractDocsTest {

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void document() throws Exception {
        GrammarDto grammarDto = new GrammarDto(
                List.of("(", ")"),
                List.of("S"),
                List.of(
                        new RuleDto("S", "(S)"),
                        new RuleDto("S", "")),
                "S");

        this.mockMvc.perform(post("/api/build/lr0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(grammarDto)))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("build/lr0", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("terminals")
                                        .description("Список терминалов грамматики. Каждый терминал должен состоять из одного символа"),
                                fieldWithPath("nonTerminals")
                                        .description("Список нетерминалов грамматики. Каждый нетерминал должен состоять из одного символа"),
                                fieldWithPath("rules[].left")
                                        .description("Нетерминал левой части правила грамматики"),
                                fieldWithPath("rules[].right")
                                        .description("Символы правой части правила грамматики"),
                                fieldWithPath("startSymbol")
                                        .description("Аксиома грамматики (нетерминал)")),
                        responseFields(
                                fieldWithPath("automaton").description("Построенный LR(0)-автомат"),
                                fieldWithPath("automaton.states").description("Список состояний автомата"),
                                fieldWithPath("automaton.states[].name").description("Имя состояния"),
                                fieldWithPath("automaton.states[].items").description("LR(0)-пункты в состоянии"),
                                fieldWithPath("automaton.states[].items[].rule")
                                        .description("Правило грамматики пункта"),
                                fieldWithPath("automaton.states[].items[].rule.left")
                                        .description("Левая часть правила"),
                                fieldWithPath("automaton.states[].items[].rule.right")
                                        .description("Правая часть правила"),
                                fieldWithPath("automaton.states[].items[].dotIndex")
                                        .description("Позиция точки в правой части правила"),
                                fieldWithPath("automaton.transitions")
                                        .description("Переходы между состояниями автомата"),
                                fieldWithPath("automaton.transitions[].from")
                                        .description("Исходное состояние"),
                                fieldWithPath("automaton.transitions[].to")
                                        .description("Целевое состояние"),
                                fieldWithPath("automaton.transitions[].through")
                                        .description("Символ перехода"),
                                fieldWithPath("buildLog")
                                        .description("Лог построения автомата. Подробнее <<buildLog,ниже>>"),
                                fieldWithPath("buildLog.operations")
                                        .description("Последовательность операций построения"),
                                fieldWithPath("buildLog.operations[].message")
                                        .description("Человекочитаемое описание операции"),
                                fieldWithPath("buildLog.operations[].level")
                                        .description("Тип операции"),
                                fieldWithPath("buildLog.operations[].name")
                                        .description("Имя операции"),
                                fieldWithPath("buildLog.operations[].stateName")
                                        .optional()
                                        .description("**Необязательное.** Имя состояния"),
                                fieldWithPath("buildLog.operations[].state")
                                        .optional()
                                        .description("**Необязательное.** Состояние, к которому применяется операция"),
                                fieldWithPath("buildLog.operations[].item")
                                        .optional()
                                        .description("**Необязательное.** LR-пункт, связанный с операцией"),
                                fieldWithPath("buildLog.operations[].item.rule.left")
                                        .optional()
                                        .description("**Необязательное.** Левая часть правила пункта"),
                                fieldWithPath("buildLog.operations[].item.rule.right")
                                        .optional()
                                        .description("**Необязательное.** Правая часть правила пункта"),
                                fieldWithPath("buildLog.operations[].item.dotIndex")
                                        .optional()
                                        .description("**Необязательное.** Позиция точки"),
                                fieldWithPath("buildLog.operations[].from")
                                        .optional()
                                        .description("**Необязательное.** Исходное состояние перехода"),
                                fieldWithPath("buildLog.operations[].to")
                                        .optional()
                                        .description("**Необязательное.** Целевое состояние перехода"),
                                fieldWithPath("buildLog.operations[].through")
                                        .optional()
                                        .description("**Необязательное.** Символ перехода"))));
    }
}
