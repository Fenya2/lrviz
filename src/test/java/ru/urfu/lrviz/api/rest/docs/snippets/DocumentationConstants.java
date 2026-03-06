package ru.urfu.lrviz.api.rest.docs.snippets;

import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.RequestDocumentation;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;
import static ru.urfu.lrviz.api.AutomatonBuildController.DEFAULT_IMAGE_SIZE;

/**
 * @author fenya
 * @since 04.03.2026
 */
public class DocumentationConstants {
    public static final String DEFAULT_VALUE_ATTRIBUTE = "defaultValue";

    public static final ParameterDescriptor VERSION_PARAMETER = RequestDocumentation.parameterWithName("version").description("версия API");
    public static final ParameterDescriptor SIZE_PARAMETER = RequestDocumentation.parameterWithName("size").optional().description("*Необязательный*. Размер изображения").attributes(key(DEFAULT_VALUE_ATTRIBUTE).value(DEFAULT_IMAGE_SIZE));

    public static final HeaderDescriptor ACCEPT_JSON_HEADER = HeaderDocumentation.headerWithName(HttpHeaders.ACCEPT).description("Всегда `%s`".formatted(APPLICATION_JSON_VALUE));
    public static final HeaderDescriptor ACCEPT_PNG_HEADER = HeaderDocumentation.headerWithName(HttpHeaders.ACCEPT).description("Всегда `%s`".formatted(IMAGE_PNG_VALUE));
    public static final HeaderDescriptor CONTENT_TYPE_JSON_HEADER = HeaderDocumentation.headerWithName(HttpHeaders.CONTENT_TYPE).description("Всегда %s".formatted(APPLICATION_JSON_VALUE));
    public static final HeaderDescriptor CONTENT_TYPE_PNG_HEADER = HeaderDocumentation.headerWithName(HttpHeaders.CONTENT_TYPE).description("Всегда %s".formatted(IMAGE_PNG_VALUE));

    public static final List<FieldDescriptor> GRAMMAR_DTO = List.of(
            fieldWithPath("terminals").description("Список <<terminals,терминалов>> грамматики."),
            fieldWithPath("nonTerminals").description("Список <<nonTerminals,нетерминалов>> грамматики."),
            fieldWithPath("rules").description("Список <<rules,правил>> грамматики"),
            fieldWithPath("rules[].left").description("Нетерминал левой части правила грамматики"),
            fieldWithPath("rules[].right").description("Символы правой части правила грамматики"),
            fieldWithPath("startSymbol").description("<<startSymbol,Аксиома>> грамматики (нетерминал)"));

    public static final List<FieldDescriptor> BUILD_LR0_LOG_DTO = List.of(
            fieldWithPath("buildLog").description("<<buildLog,Лог построения автомата>>"),
            fieldWithPath("buildLog.operations").description("Последовательность операций построения"),
            fieldWithPath("buildLog.operations[].message").description("Человекочитаемое описание операции"),
            fieldWithPath("buildLog.operations[].level").description("Тип операции"),
            fieldWithPath("buildLog.operations[].name").description("Имя операции"),
            fieldWithPath("buildLog.operations[].stateName").optional().description("**Необязательное.** Имя состояния"),
            fieldWithPath("buildLog.operations[].state").optional().description("**Необязательное.** Состояние, к которому применяется операция"),
            fieldWithPath("buildLog.operations[].item").optional().description("**Необязательное.** LR-пункт, связанный с операцией"),
            fieldWithPath("buildLog.operations[].item.rule.left").optional().description("**Необязательное.** Левая часть правила пункта"),
            fieldWithPath("buildLog.operations[].item.rule.right").optional().description("**Необязательное.** Правая часть правила пункта"),
            fieldWithPath("buildLog.operations[].item.dotIndex").optional().description("**Необязательное.** Позиция точки"),
            fieldWithPath("buildLog.operations[].from").optional().description("**Необязательное.** Исходное состояние перехода"),
            fieldWithPath("buildLog.operations[].to").optional().description("**Необязательное.** Целевое состояние перехода"),
            fieldWithPath("buildLog.operations[].through").optional().description("**Необязательное.** Символ перехода"));

    public static final List<FieldDescriptor> BUILD_LR1_LOG_DTO = List.of(
            fieldWithPath("buildLog").description("<<buildLog,Лог построения автомата>>"),
            fieldWithPath("buildLog.operations").description("Последовательность операций построения"),
            fieldWithPath("buildLog.operations[].message").description("Человекочитаемое описание операции"),
            fieldWithPath("buildLog.operations[].level").description("Тип операции"),
            fieldWithPath("buildLog.operations[].name").description("Имя операции"),
            fieldWithPath("buildLog.operations[].stateName").optional().description("**Необязательное.** Имя состояния"),
            fieldWithPath("buildLog.operations[].state").optional().description("**Необязательное.** Состояние, к которому применяется операция"),
            fieldWithPath("buildLog.operations[].item").optional().description("**Необязательное.** LR-пункт, связанный с операцией"),
            fieldWithPath("buildLog.operations[].item.rule.left").optional().description("**Необязательное.** Левая часть правила пункта"),
            fieldWithPath("buildLog.operations[].item.rule.right").optional().description("**Необязательное.** Правая часть правила пункта"),
            fieldWithPath("buildLog.operations[].item.dotIndex").optional().description("**Необязательное.** Позиция точки"),
            fieldWithPath("buildLog.operations[].item.lookAheadSymbol").optional().description("**Необязательное.** Терминальный символ (или символ завершения строки `⊣`)"),
            fieldWithPath("buildLog.operations[].from").optional().description("**Необязательное.** Исходное состояние перехода"),
            fieldWithPath("buildLog.operations[].to").optional().description("**Необязательное.** Целевое состояние перехода"),
            fieldWithPath("buildLog.operations[].through").optional().description("**Необязательное.** Символ перехода"));

    public static final List<FieldDescriptor> LR0_AUTOMATON_DTO = List.of(
            fieldWithPath("automaton").description("Построенный <<automaton,LR(0)-автомат>>"),
            fieldWithPath("automaton.states").description("<<automatonStates,Состояния>> автомата"),
            fieldWithPath("automaton.states[].name").description("Имя состояния"),
            fieldWithPath("automaton.states[].items").description("<<lrItems,LR(0)-пункты>> в состоянии"),
            fieldWithPath("automaton.states[].items[].rule").description("Правило грамматики пункта"),
            fieldWithPath("automaton.states[].items[].rule.left").description("Левая часть правила"),
            fieldWithPath("automaton.states[].items[].rule.right").description("Правая часть правила"),
            fieldWithPath("automaton.states[].items[].dotIndex").description("Позиция точки в правой части правила"),
            fieldWithPath("automaton.transitions").description("<<automatonTransitions,Переходы>> между состояниями автомата"),
            fieldWithPath("automaton.transitions[].from").description("Исходное состояние"),
            fieldWithPath("automaton.transitions[].to").description("Целевое состояние"),
            fieldWithPath("automaton.transitions[].through").description("Символ перехода"));

    public static final List<FieldDescriptor> LR1_AUTOMATON_DTO = List.of(
            fieldWithPath("automaton").description("Построенный <<automaton,LR(0)-автомат>>"),
            fieldWithPath("automaton.states").description("<<automatonStates,Состояния>> автомата"),
            fieldWithPath("automaton.states[].name").description("Имя состояния"),
            fieldWithPath("automaton.states[].items").description("<<lrItems,LR(0)-пункты>> в состоянии"),
            fieldWithPath("automaton.states[].items[].rule").description("Правило грамматики пункта"),
            fieldWithPath("automaton.states[].items[].rule.left").description("Левая часть правила"),
            fieldWithPath("automaton.states[].items[].rule.right").description("Правая часть правила"),
            fieldWithPath("automaton.states[].items[].dotIndex").description("Позиция точки в правой части правила"),
            fieldWithPath("automaton.states[].items[].lookAheadSymbol").description("Терминальный символ (или символ завершения строки `⊣`)"),
            fieldWithPath("automaton.transitions").description("<<automatonTransitions,Переходы>> между состояниями автомата"),
            fieldWithPath("automaton.transitions[].from").description("Исходное состояние"),
            fieldWithPath("automaton.transitions[].to").description("Целевое состояние"),
            fieldWithPath("automaton.transitions[].through").description("Символ перехода"));

    private DocumentationConstants() {
    }
}
