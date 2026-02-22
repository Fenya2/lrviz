package ru.urfu.lrviz.api.rest.docs;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.urfu.lrviz.core.GrammarExamples.G_6;
import static ru.urfu.lrviz.core.GrammarExamples.getJsonDto;

/**
 *
 * @author fenya
 * @since 23.02.2026
 */
class BuildLR1DocsTest extends AbstractMethodDocsTest {
    private static final String DOCUMENTED_PATH = "/api/build/lr1";

    @Override
    protected HttpMethod getDocumentedMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected String getDocumentedPath() {
        return DOCUMENTED_PATH;
    }

    @Test
    void document() throws Exception {
        this.mockMvc.perform(post(DOCUMENTED_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonDto(G_6)))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document(getSnippetPath(), preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("terminals")
                                        .description("Список <<terminals,терминалов>> грамматики."),
                                fieldWithPath("nonTerminals")
                                        .description("Список <<nonTerminals,нетерминалов>> грамматики."),
                                fieldWithPath("rules")
                                        .description("Список <<rules,правил>> грамматики"),
                                fieldWithPath("rules[].left")
                                        .description("Нетерминал левой части правила грамматики"),
                                fieldWithPath("rules[].right")
                                        .description("Символы правой части правила грамматики"),
                                fieldWithPath("startSymbol")
                                        .description("<<startSymbol,Аксиома>> грамматики (нетерминал)")),
                        responseFields(
                                fieldWithPath("automaton").description("Построенный <<automaton,LR(0)-автомат>>"),
                                fieldWithPath("automaton.states").description("<<automatonStates,Состояния>> автомата"),
                                fieldWithPath("automaton.states[].name").description("Имя состояния"),
                                fieldWithPath("automaton.states[].items").description("<<lrItems,LR(0)-пункты>> в состоянии"),
                                fieldWithPath("automaton.states[].items[].rule")
                                        .description("Правило грамматики пункта"),
                                fieldWithPath("automaton.states[].items[].rule.left")
                                        .description("Левая часть правила"),
                                fieldWithPath("automaton.states[].items[].rule.right")
                                        .description("Правая часть правила"),
                                fieldWithPath("automaton.states[].items[].dotIndex")
                                        .description("Позиция точки в правой части правила"),
                                fieldWithPath("automaton.states[].items[].lookAheadSymbol")
                                        .description("Терминальный символ (или символ завершения строки `⊣`)"),
                                fieldWithPath("automaton.transitions")
                                        .description("<<automatonTransitions,Переходы>> между состояниями автомата"),
                                fieldWithPath("automaton.transitions[].from")
                                        .description("Исходное состояние"),
                                fieldWithPath("automaton.transitions[].to")
                                        .description("Целевое состояние"),
                                fieldWithPath("automaton.transitions[].through")
                                        .description("Символ перехода"),
                                fieldWithPath("buildLog")
                                        .description("<<buildLog,Лог построения автомата>>"),
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
                                fieldWithPath("buildLog.operations[].item.lookAheadSymbol")
                                        .optional()
                                        .description("**Необязательное.** Терминальный символ (или символ завершения строки `⊣`)"),
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
