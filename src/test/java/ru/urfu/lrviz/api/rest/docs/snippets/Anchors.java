package ru.urfu.lrviz.api.rest.docs.snippets;

/**
 * Якори на определенные части документа <pre>src/main/resources/api/docs/guide.adoc</pre>
 *
 * @author fenya
 * @since 09.03.2026
 */
public class Anchors {
    public static final String TERMINALS = "terminals";
    public static final String NONTERMINALS = "nonTerminals";
    public static final String RULES = "rules";
    public static final String START_SYMBOL = "startSymbol";
    public static final String BUILD_OPTIONS = "buildOptions";
    public static final String BUILD_OPTION_NAMES_GENERATION_STRATEGY = "buildOption1";
    public static final String BUILD_OPTION_LALR1_BUILD_ALGORITHM = "buildOption2";
    public static final String AUTOMATON = "automaton";
    public static final String AUTOMATON_STATES = "automatonStates";
    public static final String LR_ITEMS = "lrItems";
    public static final String AUTOMATON_TRANSITIONS = "automatonTransitions";
    public static final String BUILD_LOG = "buildLog";

    public static String createHyperLink(String link, String text) {
        return "<<" + link + "," + text + ">>";
    }
}
