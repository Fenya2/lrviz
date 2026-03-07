package ru.urfu.lrviz.api.rest.docs.snippets;

import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.restdocs.snippet.Attributes;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static ru.urfu.lrviz.api.AutomatonBuildController.DEFAULT_IMAGE_SIZE;

/**
 * @author fenya
 * @since 04.03.2026
 */
public class DocumentationConstants {
    public static final String DEFAULT_VALUE_ATTRIBUTE_KEY = "defaultValue";

    public static final String IS_REQUIRED_ATTRIBUTE_KEY = "isRequired";
    public static final Attributes.Attribute IS_REQUIRED = Attributes.key(IS_REQUIRED_ATTRIBUTE_KEY).value("Да");
    public static final Attributes.Attribute IS_OPTIONAL = Attributes.key(IS_REQUIRED_ATTRIBUTE_KEY).value("Нет");

    public static final ParameterDescriptor VERSION_PARAMETER = RequestDocumentation.parameterWithName("version").description("версия API");
    public static final ParameterDescriptor SIZE_PARAMETER = RequestDocumentation.parameterWithName("size").optional().description("Размер изображения").attributes(key(DEFAULT_VALUE_ATTRIBUTE_KEY).value(DEFAULT_IMAGE_SIZE));

    public static final HeaderDescriptor ACCEPT_JSON_HEADER = HeaderDocumentation.headerWithName(HttpHeaders.ACCEPT).description(APPLICATION_JSON_VALUE);
    public static final HeaderDescriptor ACCEPT_PNG_HEADER = HeaderDocumentation.headerWithName(HttpHeaders.ACCEPT).description(IMAGE_PNG_VALUE);
    public static final HeaderDescriptor CONTENT_TYPE_JSON_HEADER = HeaderDocumentation.headerWithName(HttpHeaders.CONTENT_TYPE).description(APPLICATION_JSON_VALUE);
    public static final HeaderDescriptor CONTENT_TYPE_PNG_HEADER = HeaderDocumentation.headerWithName(HttpHeaders.CONTENT_TYPE).description(IMAGE_PNG_VALUE);

    public static final RequestFieldsSnippet BUILD_LR_AUTOMATON_REQUEST = requestFields()
            .andWithPrefix("grammar",
                    fieldWithPath("").description("Грамматика, по которой требуется построить LR-автомат"),
                    fieldWithPath(".terminals").description("Список <<terminals,терминалов>> грамматики."),
                    fieldWithPath(".nonTerminals").description("Список <<nonTerminals,нетерминалов>> грамматики."),
                    fieldWithPath(".rules").description("Список <<rules,правил>> грамматики"),
                    fieldWithPath(".rules[].left").description("Нетерминал левой части правила грамматики"),
                    fieldWithPath(".rules[].right").description("Символы правой части правила грамматики"),
                    fieldWithPath(".startSymbol").description("<<startSymbol,Аксиома>> грамматики (нетерминал)"))
            .andWithPrefix("buildOptions",
                    fieldWithPath("").description("Параметры построения LR-автомата").type(OBJECT).optional(),
                    fieldWithPath(".namesGenerationStrategy").type(STRING).description("Стратегия генерации имен состояний при построении автомата"));

    public static final ResponseFieldsSnippet BUILD_LR0_AUTOMATON_RESPONSE = responseFields()
            .andWithPrefix("automaton",
                    fieldWithPath("").description("Построенный <<automaton,LR(0)-автомат>>").attributes(IS_REQUIRED),
                    fieldWithPath(".states").description("<<automatonStates,Состояния>> автомата").attributes(IS_REQUIRED),
                    fieldWithPath(".states[].name").description("Имя состояния").attributes(IS_REQUIRED),
                    fieldWithPath(".states[].items").description("<<lrItems,LR(0)-пункты>> в состоянии").attributes(IS_REQUIRED),
                    fieldWithPath(".states[].items[].rule").description("Правило грамматики пункта").attributes(IS_REQUIRED),
                    fieldWithPath(".states[].items[].rule.left").description("Левая часть правила").attributes(IS_REQUIRED),
                    fieldWithPath(".states[].items[].rule.right").description("Правая часть правила").attributes(IS_REQUIRED),
                    fieldWithPath(".states[].items[].dotIndex").description("Позиция точки в правой части правила").attributes(IS_REQUIRED),
                    fieldWithPath(".transitions").description("<<automatonTransitions,Переходы>> между состояниями автомата").attributes(IS_REQUIRED),
                    fieldWithPath(".transitions[].from").description("Исходное состояние").attributes(IS_REQUIRED),
                    fieldWithPath(".transitions[].to").description("Целевое состояние").attributes(IS_REQUIRED),
                    fieldWithPath(".transitions[].through").description("Символ перехода").attributes(IS_REQUIRED))
            .andWithPrefix("buildLog",
                    fieldWithPath("").description("<<buildLog,Лог построения автомата>>").attributes(IS_REQUIRED),
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
            fieldWithPath("automaton.states[].items[].lookAheadSymbol").description("Терминальный символ (или символ завершения строки `⊣`)").attributes(IS_REQUIRED),
            fieldWithPath("buildLog.operations[].item.lookAheadSymbol").optional().description("Терминальный символ (или символ завершения строки `⊣`)").attributes(IS_OPTIONAL));

    private DocumentationConstants() {
    }
}
