package ru.urfu.lrviz.core;

import ru.urfu.lrviz.core.grammar.Grammar;
import ru.urfu.lrviz.core.grammar.NonTerminal;
import ru.urfu.lrviz.core.grammar.Rule;
import ru.urfu.lrviz.core.grammar.Terminal;

import java.util.Map;
import java.util.Set;

/**
 * @author fenya
 * @since 15.02.2026
 */
public class GrammarExamples {

    /**
     * <pre>
     * D => T L
     * T => int | real
     * L => L ; a | a
     * </pre>
     */
    public static final String G_1 = "g1";

    public static final Map<String, Grammar> EXAMPLES = Map.of(G_1, getG1());

    public static Grammar get(String name) {
        return EXAMPLES.get(name);
    }


    private static Grammar getG1() {
        Terminal INT = new Terminal("int");
        Terminal REAL = new Terminal("real");
        Terminal SEMICOLON = new Terminal(";");
        Terminal a = new Terminal("a");
        Set<Terminal> terminals = Set.of(INT, REAL, SEMICOLON, a);

        NonTerminal D = new NonTerminal("D");
        NonTerminal T = new NonTerminal("T");
        NonTerminal L = new NonTerminal("L");
        Set<NonTerminal> nonTerminals = Set.of(D, T, L);

        Rule r1 = new Rule(D, T, L);
        Rule r2 = new Rule(T, REAL);
        Rule r3 = new Rule(T, INT);
        Rule r4 = new Rule(L, L, SEMICOLON, a);
        Rule r5 = new Rule(L, a);
        Set<Rule> rules = Set.of(r1, r2, r3, r4, r5);

        return new Grammar(terminals, nonTerminals, rules, D);
    }

    private GrammarExamples() {

    }
}
