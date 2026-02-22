package ru.urfu.lrviz.core;

import ru.urfu.lrviz.core.grammar.Grammar;
import ru.urfu.lrviz.core.grammar.NonTerminal;
import ru.urfu.lrviz.core.grammar.Rule;
import ru.urfu.lrviz.core.grammar.Terminal;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author fenya
 * @since 15.02.2026
 */
@SuppressWarnings("java:S117") // имена переменных здесь оправданы
public class GrammarExamples {

    public static final String JSON_RESOURCES_PATH = "/grammars/api";

    /**
     * <pre>
     * D => T L
     * T => i | r
     * L => L ; a | a
     * </pre>
     */
    public static final String G_1 = "g1";

    /**
     * <pre>
     * S => A A;
     * A => a A | b;
     * </pre>
     */
    public static final String G_2 = "g2";

    /**
     * <pre>
     * S => S ( A ) S | ;
     * A => B C | ;
     * B => b;
     * C => c | ;
     * </pre>
     */
    public static final String G_3 = "g3";

    /**
     * <pre>
     * S => u B D z;
     * B => B v | w;
     * D => E F;
     * E => y | ;
     * F => x | ;
     * </pre>
     */
    public static final String G_4 = "g4";

    /**
     * <pre>
     * S => ( S );
     * S => ;
     * </pre>
     */
    public static final String G_5 = "g5";

    /**
     * <pre>
     * S => A a | b A c | b c | b d a;
     * A => d;
     * </pre>
     */
    public static final String G_6 = "g6";

    /**
     * <pre>
     * S => A B C;
     * A => A a | a;
     * B => B b | b;
     * C => C c | c;
     * </pre>
     */
    public static final String G_7 = "g7";

    /**
     * <pre>
     * E => T | E + T;
     * T => i | ( E );
     * </pre>
     */
    public static final String G_8 = "g8";

    public static final Map<String, Grammar> EXAMPLES = Map.of(
            G_1, createG1(),
            G_2, createG2(),
            G_3, createG3(),
            G_4, createG4(),
            G_5, createG5(),
            G_6, createG6(),
            G_7, createG7(),
            G_8, createG8());

    public static Grammar get(String name) {
        return EXAMPLES.get(name);
    }

    public static String getJsonDto(String grammarName) {
        try {
            URL resource = Objects.requireNonNull(GrammarExamples.class.getResource(getResourcePath(grammarName)));
            Path path = Paths.get(resource.toURI());
            return Files.readString(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getResourcePath(String grammarName) {
        return JSON_RESOURCES_PATH + "/" + grammarName + ".json";
    }

    private static Grammar createG1() {
        Terminal INT = new Terminal("i");
        Terminal REAL = new Terminal("r");
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

    private static Grammar createG2() {
        Terminal a = new Terminal("a");
        Terminal b = new Terminal("b");
        Set<Terminal> terminals = Set.of(a, b);

        NonTerminal S = new NonTerminal("S");
        NonTerminal A = new NonTerminal("A");
        Set<NonTerminal> nonTerminals = Set.of(S, A);

        Rule r1 = new Rule(S, A, A);
        Rule r2 = new Rule(A, a, A);
        Rule r3 = new Rule(A, b);
        Set<Rule> rules = Set.of(r1, r2, r3);

        return new Grammar(terminals, nonTerminals, rules, S);
    }

    /**
     * <pre>
     * S => S ( A ) S | ;
     * A => B C | ;
     * B => b;
     * C => c | ;
     * </pre>
     */
    private static Grammar createG3() {
        Terminal LPAREN = new Terminal("(");
        Terminal RPAREN = new Terminal(")");
        Terminal b = new Terminal("b");
        Terminal c = new Terminal("c");
        Set<Terminal> terminals = Set.of(LPAREN, RPAREN, b, c);

        NonTerminal S = new NonTerminal("S");
        NonTerminal A = new NonTerminal("A");
        NonTerminal B = new NonTerminal("B");
        NonTerminal C = new NonTerminal("C");
        Set<NonTerminal> nonTerminals = Set.of(S, A, B, C);

        Rule r1 = new Rule(S, S, LPAREN, A, RPAREN, S);
        Rule r2 = new Rule(S);
        Rule r3 = new Rule(A, B, C);
        Rule r4 = new Rule(A);
        Rule r5 = new Rule(B, b);
        Rule r6 = new Rule(C, c);
        Rule r7 = new Rule(C);
        Set<Rule> rules = Set.of(r1, r2, r3, r4, r5, r6, r7);

        return new Grammar(terminals, nonTerminals, rules, S);
    }

    private static Grammar createG4() {
        Terminal v = new Terminal("v");
        Terminal u = new Terminal("u");
        Terminal w = new Terminal("w");
        Terminal x = new Terminal("x");
        Terminal y = new Terminal("y");
        Terminal z = new Terminal("z");
        Set<Terminal> terminals = Set.of(v, u, w, x, y, z);

        NonTerminal S = new NonTerminal("S");
        NonTerminal B = new NonTerminal("B");
        NonTerminal D = new NonTerminal("D");
        NonTerminal E = new NonTerminal("E");
        NonTerminal F = new NonTerminal("F");
        Set<NonTerminal> nonTerminals = Set.of(S, B, D, E, F);

        Rule r1 = new Rule(S, u, B, D, z);
        Rule r2 = new Rule(B, B, v);
        Rule r3 = new Rule(B, w);
        Rule r4 = new Rule(D, E, F);
        Rule r5 = new Rule(E, y);
        Rule r6 = new Rule(E);
        Rule r7 = new Rule(F, x);
        Rule r8 = Rule.ofEmpty(F);
        Set<Rule> rules = Set.of(r1, r2, r3, r4, r5, r6, r7, r8);

        return new Grammar(terminals, nonTerminals, rules, S);
    }

    private static Grammar createG5() {
        Terminal LPAREN = new Terminal("(");
        Terminal RPAREN = new Terminal(")");
        Set<Terminal> terminals = Set.of(LPAREN, RPAREN);

        NonTerminal S = new NonTerminal("S");
        Set<NonTerminal> nonTerminals = Set.of(S);

        Rule r1 = new Rule(S, LPAREN, S, RPAREN);
        Rule r2 = new Rule(S);
        Set<Rule> rules = Set.of(r1, r2);

        return new Grammar(terminals, nonTerminals, rules, S);
    }

    private static Grammar createG6() {
        Terminal a = new Terminal("a");
        Terminal b = new Terminal("b");
        Terminal c = new Terminal("c");
        Terminal d = new Terminal("d");
        Set<Terminal> terminals = Set.of(a, b, c, d);

        NonTerminal S = new NonTerminal("S");
        NonTerminal A = new NonTerminal("A");
        Set<NonTerminal> nonTerminals = Set.of(S, A);

        Rule r1 = new Rule(S, A, a);
        Rule r2 = new Rule(S, b, A, c);
        Rule r3 = new Rule(S, b, c);
        Rule r4 = new Rule(S, b, d, a);
        Rule r5 = new Rule(A, d);
        Set<Rule> rules = Set.of(r1, r2, r3, r4, r5);

        return new Grammar(terminals, nonTerminals, rules, S);
    }

    private static Grammar createG7() {
        Terminal a = new Terminal("a");
        Terminal b = new Terminal("b");
        Terminal c = new Terminal("c");
        Set<Terminal> terminals = Set.of(a, b, c);

        NonTerminal S = new NonTerminal("S");
        NonTerminal A = new NonTerminal("A");
        NonTerminal B = new NonTerminal("B");
        NonTerminal C = new NonTerminal("C");
        Set<NonTerminal> nonTerminals = Set.of(S, A, B, C);

        Rule r1 = new Rule(S, A, B, C);
        Rule r2 = new Rule(A, A, a);
        Rule r3 = new Rule(A, a);
        Rule r4 = new Rule(B, B, b);
        Rule r5 = new Rule(B, b);
        Rule r6 = new Rule(C, C, c);
        Rule r7 = new Rule(C, c);
        Set<Rule> rules = Set.of(r1, r2, r3, r4, r5, r6, r7);

        return new Grammar(terminals, nonTerminals, rules, S);
    }

    private static Grammar createG8() {
        Terminal i = new Terminal("i");
        Terminal plus = new Terminal("+");
        Terminal lParen = new Terminal("(");
        Terminal rParen = new Terminal(")");
        Set<Terminal> terminals = Set.of(i, plus, lParen, rParen);

        NonTerminal E = new NonTerminal("E");
        NonTerminal T = new NonTerminal("T");
        Set<NonTerminal> nonTerminals = Set.of(E, T);

        Rule r1 = new Rule(E, T);
        Rule r2 = new Rule(E, E, plus, T);
        Rule r3 = new Rule(T, i);
        Rule r4 = new Rule(T, lParen, E, rParen);
        Set<Rule> rules = Set.of(r1, r2, r3, r4);

        return new Grammar(terminals, nonTerminals, rules, E);
    }

    private GrammarExamples() {

    }
}
