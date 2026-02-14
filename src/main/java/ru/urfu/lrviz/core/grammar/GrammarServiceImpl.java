package ru.urfu.lrviz.core.grammar;

import org.springframework.stereotype.Service;

import java.util.*;

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

    @Override
    public Map<GrammarSymbol, Set<FirstSetMember>> getFirst(Grammar grammar) {
        Map<GrammarSymbol, Set<FirstSetMember>> result = HashMap.newHashMap(
                grammar.getTerminals().size() + grammar.getNonTerminals().size());

        initFirst(grammar, result);

        boolean stabilized;
        do {
            stabilized = true;
            for (Rule rule : grammar.getRules()) {
                Set<FirstSetMember> growingSet = result.get(rule.left());
                int beforeGrowAttemptSize = growingSet.size();
                tryToGrowUp(growingSet, rule.right(), result);
                if (beforeGrowAttemptSize < growingSet.size()) {
                    stabilized = false;
                }
            }
        } while (!stabilized);
        return result;
    }

    private static void initFirst(Grammar grammar, Map<GrammarSymbol, Set<FirstSetMember>> result) {
        for (Terminal terminal : grammar.getTerminals()) {
            result.put(terminal, Collections.singleton(terminal));
        }

        for (Rule rule : grammar.getRules()) {
            Set<FirstSetMember> firstSet = new HashSet<>();
            if (rule.isEmpty()) {
                firstSet.add(Epsilon.getInstance());
            }
            result.put(rule.left(), firstSet);
        }
    }

    private static void tryToGrowUp(Set<FirstSetMember> growingSet, List<GrammarSymbol> right, Map<GrammarSymbol, Set<FirstSetMember>> currentFirstSet) {
        int i = 0;
        for (; i < right.size(); i++) {
            Set<FirstSetMember> firstSetMembers = currentFirstSet.get(right.get(i));
            Set<FirstSetMember> newMembers = new HashSet<>(firstSetMembers);
            newMembers.remove(Epsilon.getInstance());
            growingSet.addAll(newMembers);
            if (!firstSetMembers.contains(Epsilon.getInstance())) {
                break;
            }
        }
        if (i == right.size()) {
            growingSet.add(Epsilon.getInstance());
        }
    }
}
