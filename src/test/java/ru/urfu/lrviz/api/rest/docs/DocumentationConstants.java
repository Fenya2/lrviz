package ru.urfu.lrviz.api.rest.docs;

import org.springframework.restdocs.payload.FieldDescriptor;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

/**
 * @author fenya
 * @since 04.03.2026
 */
public class DocumentationConstants {
    public static final FieldDescriptor[] GRAMMAR_DTO = {
            fieldWithPath("terminals").description("Список <<terminals,терминалов>> грамматики."),
            fieldWithPath("nonTerminals").description("Список <<nonTerminals,нетерминалов>> грамматики."),
            fieldWithPath("rules").description("Список <<rules,правил>> грамматики"),
            fieldWithPath("rules[].left").description("Нетерминал левой части правила грамматики"),
            fieldWithPath("rules[].right").description("Символы правой части правила грамматики"),
            fieldWithPath("startSymbol").description("<<startSymbol,Аксиома>> грамматики (нетерминал)")};

    private DocumentationConstants() {

    }
}
