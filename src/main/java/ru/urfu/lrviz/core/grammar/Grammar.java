package ru.urfu.lrviz.core.grammar;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Grammar {
    private final Set<Terminal> terminals;
    private final Set<NonTerminal> nonTerminals;
    private final Set<Rule> rules;
    private NonTerminal startSymbol;

    public Grammar(Set<Terminal> terminals, Set<NonTerminal> nonTerminals, Set<Rule> rules, NonTerminal startSymbol) {
        checkRuleSet(terminals, nonTerminals, rules);
        checkStartSymbol(nonTerminals, startSymbol);
        this.terminals = new HashSet<>(terminals);
        this.nonTerminals = new HashSet<>(nonTerminals);
        this.rules = new HashSet<>(rules);
        this.startSymbol = startSymbol;
    }

    public Grammar(Grammar grammar) {
        this.terminals = new HashSet<>(grammar.terminals);
        this.nonTerminals = new HashSet<>(grammar.nonTerminals);
        this.rules = new HashSet<>(grammar.rules);
        this.startSymbol = grammar.startSymbol;
    }

    private void checkRuleSet(Set<Terminal> terminals, Set<NonTerminal> nonTerminals, Set<Rule> rules) {
        Set<NonTerminal> leftParts = rules.stream().map(Rule::left).collect(Collectors.toSet());
        if (!nonTerminals.containsAll(leftParts)) {
            throw new IllegalArgumentException(
                    "Left parts of rules contains nonterminal not included in nonterminals set.");
        }
        Set<GrammarSymbol> rightPartsSymbols = rules.stream()
                .map(Rule::right)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        Set<GrammarSymbol> permittedGrammarSymbols = new HashSet<>(terminals);
        permittedGrammarSymbols.addAll(nonTerminals);

        if (!permittedGrammarSymbols.containsAll(rightPartsSymbols)) {
            throw new IllegalArgumentException(
                    "Right parts of rules contains symbol not included in any grammar alphabet.");
        }
    }

    private void checkStartSymbol(Set<NonTerminal> nonTerminals, NonTerminal startSymbol) {
        if (!nonTerminals.contains(startSymbol)) {
            throw new IllegalArgumentException("Nonterminals don't contains start symbol.");
        }
    }

    public void addNonTerminal(NonTerminal symbol) {
        this.nonTerminals.add(symbol);
    }

    public void addRule(Rule rule) {
        if (!nonTerminals.contains(rule.left())) {
            throw new IllegalArgumentException("Nonterminals don't contains rule left part.");
        }
        for (GrammarSymbol symbol : rule.right()) {
            if (switch (symbol) {
                case Terminal t -> terminals.contains(t);
                case NonTerminal nt -> nonTerminals.contains(nt);
            }) {
                continue;
            }
            throw new IllegalArgumentException("Nonterminals don't contains rule right part.");
        }
        rules.add(rule);
    }

    public void setStartSymbol(NonTerminal newStartSymbol) {
        if (!nonTerminals.contains(newStartSymbol)) {
            throw new IllegalArgumentException("Current nonterminals set don't contains new start symbol.");
        }
        this.startSymbol = newStartSymbol;
    }

    public Set<Terminal> getTerminals() {
        return Collections.unmodifiableSet(terminals);
    }

    public Set<NonTerminal> getNonTerminals() {
        return Collections.unmodifiableSet(nonTerminals);
    }

    public Set<Rule> getRules() {
        return Collections.unmodifiableSet(rules);
    }

    public NonTerminal getStartSymbol() {
        return startSymbol;
    }

    public Set<Rule> getAlternativesFor(NonTerminal leftPart) {
        return rules.stream().filter(rule -> rule.left().equals(leftPart)).collect(Collectors.toSet());
    }
}