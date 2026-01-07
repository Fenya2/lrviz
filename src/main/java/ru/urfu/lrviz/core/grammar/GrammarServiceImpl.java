package ru.urfu.lrviz.core.grammar;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;

@Service
public class GrammarServiceImpl implements GrammarService {
    @Override
    public void extendGrammar(Grammar grammar) {
        NonTerminal oldStartSymbol = grammar.getStartSymbol();
        NonTerminal newStartSymbol = prepareNewStartSymbol(grammar);
        grammar.addNonTerminal(newStartSymbol);
        grammar.setStartSymbol(newStartSymbol);
        ArrayList<GrammarSymbol> right = new ArrayList<>();
        right.add(oldStartSymbol);
        grammar.addRule(new Rule(newStartSymbol, right));
    }

    private NonTerminal prepareNewStartSymbol(Grammar grammar) {
        StringBuilder newStartSymbol = new StringBuilder(grammar.getStartSymbol().lexicalValue);
        Set<NonTerminal> currentNonTerminals = grammar.getNonTerminals();
        NonTerminal candidate;
        do {
            newStartSymbol.append("'");
            candidate = new NonTerminal(newStartSymbol.toString());
        } while (currentNonTerminals.contains(candidate));
        return candidate;
    }
}
