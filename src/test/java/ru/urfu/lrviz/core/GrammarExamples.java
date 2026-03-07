package ru.urfu.lrviz.core;

import ru.urfu.lrviz.api.dto.GrammarDto;
import ru.urfu.lrviz.api.dto.RuleDto;
import ru.urfu.lrviz.core.grammar.Grammar;
import ru.urfu.lrviz.core.grammar.NonTerminal;
import ru.urfu.lrviz.core.grammar.Rule;
import ru.urfu.lrviz.core.grammar.Terminal;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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
     * S => A A
     * A => a A | b
     * </pre>
     */
    public static final String G_2 = "g2";

    /**
     * <pre>
     * S => S ( A ) S |
     * A => B C |
     * B => b
     * C => c |
     * </pre>
     */
    public static final String G_3 = "g3";

    /**
     * <pre>
     * S => u B D z
     * B => B v | w
     * D => E F
     * E => y |
     * F => x |
     * </pre>
     */
    public static final String G_4 = "g4";

    /**
     * <pre>
     * S => ( S )
     * S =>
     * </pre>
     */
    public static final String G_5 = "g5";

    /**
     * <pre>
     * S => A a | b A c | b c | b d a
     * A => d
     * </pre>
     */
    public static final String G_6 = "g6";

    /**
     * <pre>
     * S => A B C
     * A => A a | a
     * B => B b | b
     * C => C c | c
     * </pre>
     */
    public static final String G_7 = "g7";

    /**
     * <pre>
     * E => T | E + T
     * T => i | ( E )
     * </pre>
     */
    public static final String G_8 = "g8";

    /**
     * <pre>
     * S => L=R | R
     * L => *R | x
     * R => L
     * </pre>
     */
    public static final String G_9 = "g9";

    public static final Map<String, Grammar> EXAMPLES = Map.of(
            G_1, createG1(),
            G_2, createG2(),
            G_3, createG3(),
            G_4, createG4(),
            G_5, createG5(),
            G_6, createG6(),
            G_7, createG7(),
            G_8, createG8(),
            G_9, createG9());

    public static final Map<String, GrammarDto> DTO_EXAMPLES = Map.of(
            G_1, createG1Dto()
    );

    public static Grammar get(String name) {
        return EXAMPLES.get(name);
    }

    public static GrammarDto getAsDto(String name) {
        return DTO_EXAMPLES.get(name);
    }

    public static String getAsJson(String grammarName) {
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

        Set<Rule> rules = Set.of(
                new Rule(D, T, L),
                new Rule(T, REAL),
                new Rule(T, INT),
                new Rule(L, L, SEMICOLON, a),
                new Rule(L, a));

        return new Grammar(terminals, nonTerminals, rules, D);
    }

    /**
     * <pre>
     * D => T L
     * T => i | r
     * L => L ; a | a
     * </pre>
     */
    private static GrammarDto createG1Dto() {
        return new GrammarDto(
                List.of("i", "r", ";", "a"),
                List.of("D", "T", "L"),
                List.of(new RuleDto("D", "TL"),
                        new RuleDto("T", "i"),
                        new RuleDto("T", "i"),
                        new RuleDto("L", "L;a"),
                        new RuleDto("L", "a")),
                "D"
        );
    }

    private static Grammar createG2() {
        Terminal a = new Terminal("a");
        Terminal b = new Terminal("b");
        Set<Terminal> terminals = Set.of(a, b);

        NonTerminal S = new NonTerminal("S");
        NonTerminal A = new NonTerminal("A");
        Set<NonTerminal> nonTerminals = Set.of(S, A);

        Set<Rule> rules = Set.of(
                new Rule(S, A, A),
                new Rule(A, a, A),
                new Rule(A, b));

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

        Set<Rule> rules = Set.of(
                new Rule(S, S, LPAREN, A, RPAREN, S),
                new Rule(S),
                new Rule(A, B, C),
                new Rule(A),
                new Rule(B, b),
                new Rule(C, c),
                new Rule(C));

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

        Set<Rule> rules = Set.of(
                new Rule(S, u, B, D, z),
                new Rule(B, B, v),
                new Rule(B, w),
                new Rule(D, E, F),
                new Rule(E, y),
                new Rule(E),
                new Rule(F, x),
                Rule.ofEmpty(F));

        return new Grammar(terminals, nonTerminals, rules, S);
    }

    private static Grammar createG5() {
        Terminal LPAREN = new Terminal("(");
        Terminal RPAREN = new Terminal(")");
        Set<Terminal> terminals = Set.of(LPAREN, RPAREN);

        NonTerminal S = new NonTerminal("S");
        Set<NonTerminal> nonTerminals = Set.of(S);

        Set<Rule> rules = Set.of(
                new Rule(S, LPAREN, S, RPAREN),
                new Rule(S));

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

        Set<Rule> rules = Set.of(
                new Rule(S, A, a),
                new Rule(S, b, A, c),
                new Rule(S, b, c),
                new Rule(S, b, d, a),
                new Rule(A, d));

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

        Set<Rule> rules = Set.of(
                new Rule(S, A, B, C),
                new Rule(A, A, a),
                new Rule(A, a),
                new Rule(B, B, b),
                new Rule(B, b),
                new Rule(C, C, c),
                new Rule(C, c));

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

        Set<Rule> rules = Set.of(
                new Rule(E, T),
                new Rule(E, E, plus, T),
                new Rule(T, i),
                new Rule(T, lParen, E, rParen));

        return new Grammar(terminals, nonTerminals, rules, E);
    }

    private static Grammar createG9() {
        Terminal x = new Terminal("x");
        Terminal asterisk = new Terminal("*");
        Terminal equals = new Terminal("=");
        Set<Terminal> terminals = Set.of(x, asterisk, equals);

        NonTerminal S = new NonTerminal("S");
        NonTerminal L = new NonTerminal("L");
        NonTerminal R = new NonTerminal("R");

        Set<NonTerminal> nonTerminals = Set.of(S, L, R);

        Set<Rule> rules = Set.of(
                new Rule(S, L, equals, R),
                new Rule(S, R),
                new Rule(L, asterisk, R),
                new Rule(L, x),
                new Rule(R, L));

        return new Grammar(terminals, nonTerminals, rules, S);
    }


    private GrammarExamples() {

    }
}
