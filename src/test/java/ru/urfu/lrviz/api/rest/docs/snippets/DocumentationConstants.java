package ru.urfu.lrviz.api.rest.docs.snippets;

import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.restdocs.snippet.Attributes;

import java.util.List;
import java.util.stream.Stream;

import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static ru.urfu.lrviz.api.rest.docs.snippets.Anchors.*;

/**
 * @author fenya
 * @since 04.03.2026
 */
public class DocumentationConstants {
    public static final String VISUALIZE_OPERATIONS_FIELD_NAME = "visualizeOperations";

    public static final String IS_REQUIRED_ATTRIBUTE_KEY = "isRequired";
    public static final Attributes.Attribute IS_REQUIRED = Attributes.key(IS_REQUIRED_ATTRIBUTE_KEY).value("Да");
    public static final Attributes.Attribute IS_OPTIONAL = Attributes.key(IS_REQUIRED_ATTRIBUTE_KEY).value("Нет");

    public static final ParameterDescriptor VERSION_PARAMETER = RequestDocumentation.parameterWithName("version").description("версия API");

    public static final HeaderDescriptor ACCEPT_JSON_HEADER = HeaderDocumentation.headerWithName(HttpHeaders.ACCEPT).description(APPLICATION_JSON_VALUE);
    public static final HeaderDescriptor ACCEPT_PNG_HEADER = HeaderDocumentation.headerWithName(HttpHeaders.ACCEPT).description(IMAGE_PNG_VALUE);
    public static final HeaderDescriptor ACCEPT_ZIP_HEADER = HeaderDocumentation.headerWithName(HttpHeaders.ACCEPT).description(APPLICATION_OCTET_STREAM_VALUE);
    public static final HeaderDescriptor CONTENT_TYPE_JSON_HEADER = HeaderDocumentation.headerWithName(HttpHeaders.CONTENT_TYPE).description(APPLICATION_JSON_VALUE);
    public static final HeaderDescriptor CONTENT_TYPE_PNG_HEADER = HeaderDocumentation.headerWithName(HttpHeaders.CONTENT_TYPE).description(IMAGE_PNG_VALUE);
    public static final HeaderDescriptor CONTENT_TYPE_ZIP_HEADER = HeaderDocumentation.headerWithName(HttpHeaders.CONTENT_TYPE).description(APPLICATION_OCTET_STREAM_VALUE + " (zip)");

    public static final List<FieldDescriptor> GRAMMAR_DTO = List.of(fieldWithPath(".terminals").description("Список %s грамматики.".formatted(createHyperLink(TERMINALS, "терминалов"))).attributes(IS_REQUIRED),
            fieldWithPath(".nonTerminals").description("Список %s грамматики".formatted(createHyperLink(NONTERMINALS, "нетерминалов"))).attributes(IS_REQUIRED),
            fieldWithPath(".rules").description("Список %s грамматики".formatted(createHyperLink(RULES, "правил"))).attributes(IS_REQUIRED),
            fieldWithPath(".rules[].left").description("Нетерминал левой части правила грамматики").attributes(IS_REQUIRED),
            fieldWithPath(".rules[].right").description("Символы правой части правила грамматики").attributes(IS_REQUIRED),
            fieldWithPath(".startSymbol").description("%s грамматики (нетерминал)".formatted(createHyperLink(START_SYMBOL, "Аксиома"))).attributes(IS_REQUIRED));

    public static final FieldDescriptor OPTION_NAMES_GENERATION_STRATEGY = fieldWithPath(".namesGenerationStrategy").optional().type(STRING).description("%s при построении автомата".formatted(createHyperLink(BUILD_OPTION_NAMES_GENERATION_STRATEGY, "Стратегия генерации имен состояний"))).attributes(IS_OPTIONAL);
    public static final FieldDescriptor OPTION_LALR1_BUILD_ALGORITHM = fieldWithPath(".lalr1BuildAlgorithm").optional().type(STRING).description(createHyperLink(BUILD_OPTION_LALR1_BUILD_ALGORITHM, "Алгоритм построения LALR(1)-автомата")).attributes(IS_OPTIONAL);
    public static final FieldDescriptor OPTION_ENABLE_BUILD_LOG = fieldWithPath(".enableBuildLog").optional().type(BOOLEAN).description("Передавать ли в ответе %s".formatted(createHyperLink(BUILD_OPTION_ENABLE_BUILD_LOG, "лог построения"))).attributes(IS_OPTIONAL);

    public static final FieldDescriptor VISUALIZE_OPTION_VISUALIZE_OPERATIONS = fieldWithPath(".visualizeOperations").optional().type(ARRAY).description("%s, которые нужно визуализировать. Если не указаны, то визуализируются все операции".formatted(createHyperLink(Anchors.VISUALIZE_OPTION_VISUALIZE_OPERATIONS, "Номера операций"))).attributes(IS_OPTIONAL);
    public static final FieldDescriptor VISUALIZE_OPTION_COLORIZE_TRANSITIONS = fieldWithPath(".colorizeTransitions").optional().type(BOOLEAN).description(createHyperLink(Anchors.VISUALIZE_OPTION_VISUALIZE_OPERATIONS, "Нужно ли окрашивать переходы автомата. " + "По умолчанию не окрашиваются.")).attributes(IS_OPTIONAL);

    public static final RequestFieldsSnippet BUILD_LR_AUTOMATON_REQUEST = requestFields()
            .andWithPrefix(".grammar",
                    Stream.concat(
                            Stream.of(fieldWithPath("").description("Грамматика, по которой требуется построить LR-автомат").attributes(IS_REQUIRED)),
                            GRAMMAR_DTO.stream()).toList())
            .andWithPrefix(".buildOptions",
                    fieldWithPath("").optional().description("%s построения LR-автомата".formatted(createHyperLink(BUILD_OPTIONS, "Параметры"))).type(OBJECT).attributes(IS_OPTIONAL),
                    OPTION_NAMES_GENERATION_STRATEGY);

    public static final RequestFieldsSnippet BUILD_LALR_AUTOMATON_REQUEST = BUILD_LR_AUTOMATON_REQUEST
            .andWithPrefix(".buildOptions", OPTION_LALR1_BUILD_ALGORITHM);

    public static final ResponseFieldsSnippet BUILD_LR0_AUTOMATON_RESPONSE = responseFields()
            .andWithPrefix(".automaton",
                    fieldWithPath("").description("Построенный %s".formatted(createHyperLink(AUTOMATON, "LR-автомат"))).attributes(IS_REQUIRED),
                    fieldWithPath(".states").description("%s автомата".formatted(createHyperLink(AUTOMATON_STATES, "Состояния"))).attributes(IS_REQUIRED),
                    fieldWithPath(".states[].name").description("Имя состояния").attributes(IS_REQUIRED),
                    fieldWithPath(".states[].items").description("%s в состоянии".formatted(createHyperLink(LR_ITEMS, "LR(0)-пункты"))).attributes(IS_REQUIRED),
                    fieldWithPath(".states[].items[].rule").description("Правило грамматики пункта").attributes(IS_REQUIRED),
                    fieldWithPath(".states[].items[].rule.left").description("Левая часть правила").attributes(IS_REQUIRED),
                    fieldWithPath(".states[].items[].rule.right").description("Правая часть правила").attributes(IS_REQUIRED),
                    fieldWithPath(".states[].items[].dotIndex").description("Позиция точки в правой части правила").attributes(IS_REQUIRED),
                    fieldWithPath(".transitions").description("%s между состояниями автомата".formatted(createHyperLink(AUTOMATON_TRANSITIONS, "Переходы"))).attributes(IS_REQUIRED),
                    fieldWithPath(".transitions[].from").description("Исходное состояние").attributes(IS_REQUIRED),
                    fieldWithPath(".transitions[].to").description("Целевое состояние").attributes(IS_REQUIRED),
                    fieldWithPath(".transitions[].through").description("Символ перехода").attributes(IS_REQUIRED))
            .andWithPrefix(".buildLog",
                    fieldWithPath("").optional().type(OBJECT).description("%s (если указана %s)".formatted(createHyperLink(BUILD_LOG, "Лог построения LR-автомата"), createHyperLink(BUILD_OPTION_ENABLE_BUILD_LOG, "соответствующая опция"))).attributes(IS_OPTIONAL),
                    fieldWithPath(".operations").description("Последовательность операций построения").attributes(IS_REQUIRED),
                    fieldWithPath(".operations[].message").description("Человекочитаемое описание операции").attributes(IS_REQUIRED),
                    fieldWithPath(".operations[].level").description("Тип операции").attributes(IS_REQUIRED),
                    fieldWithPath(".operations[].name").description("Имя операции").attributes(IS_REQUIRED),
                    fieldWithPath(".operations[].stateName").optional().description("Имя состояния").attributes(IS_OPTIONAL),
                    fieldWithPath(".operations[].state").optional().description("Состояние, к которому применяется операция").attributes(IS_OPTIONAL),
                    fieldWithPath(".operations[].item").optional().description("LR-пункт, связанный с операцией").attributes(IS_OPTIONAL),
                    fieldWithPath(".operations[].item.rule.left").optional().description("Левая часть правила пункта").attributes(IS_OPTIONAL),
                    fieldWithPath(".operations[].item.rule.right").optional().description("Правая часть правила пункта").attributes(IS_OPTIONAL),
                    fieldWithPath(".operations[].item.dotIndex").optional().description("Позиция точки").attributes(IS_OPTIONAL),
                    fieldWithPath(".operations[].from").optional().description("Исходное состояние перехода").attributes(IS_OPTIONAL),
                    fieldWithPath(".operations[].to").optional().description("Целевое состояние перехода").attributes(IS_OPTIONAL),
                    fieldWithPath(".operations[].through").optional().description("Символ перехода").attributes(IS_OPTIONAL));

    public static final ResponseFieldsSnippet BUILD_LR1_AUTOMATON_RESPONSE = BUILD_LR0_AUTOMATON_RESPONSE.and(
            fieldWithPath(".automaton.states[].items[].lookAheadSymbol").description("Символ предпросмотра").attributes(IS_OPTIONAL),
            fieldWithPath(".buildLog.operations[].item.lookAheadSymbol").optional().description("Символ предпросмотра").attributes(IS_OPTIONAL));

    public static final ResponseFieldsSnippet BUILD_LALR1_AUTOMATON_RESPONSE = BUILD_LR1_AUTOMATON_RESPONSE.and(
            fieldWithPath(".automaton.states[].items[].lookAheadSymbols")
                    .optional()
                    .type(JsonFieldType.ARRAY)
                    .description("Символы предпросмотра (при построении через %s)"
                            .formatted(createHyperLink(BUILD_OPTION_LALR1_BUILD_ALGORITHM, "канальный алгоритм")))
                    .attributes(IS_OPTIONAL));

    private DocumentationConstants() {
    }
}
