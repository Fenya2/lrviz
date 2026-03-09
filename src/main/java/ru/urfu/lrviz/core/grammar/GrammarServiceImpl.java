package ru.urfu.lrviz.core.grammar;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GrammarServiceImpl implements GrammarService {
    @Override
    public void expandGrammar(Grammar grammar) {
        NonTerminal oldStartSymbol = grammar.getStartSymbol();
        NonTerminal newStartSymbol = prepareNewStartSymbol(grammar);
        grammar.addNonTerminal(newStartSymbol);
        grammar.setStartSymbol(newStartSymbol);
        ArrayList<GrammarSymbol> right = new ArrayList<>();
        right.add(oldStartSymbol);
        grammar.addRule(new Rule(newStartSymbol, right));
    }

    public boolean isGrammarExtended(Grammar grammar) {
        Set<Rule> initRules = grammar.getAlternativesFor(grammar.getStartSymbol());
        return initRules.size() == 1 && initRules.iterator().next().right().size() == 1;
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
                calculateForChain(growingSet, rule.right(), result);
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

    public static void calculateForChain(Set<FirstSetMember> result,
                                         List<GrammarSymbol> chain,
                                         Map<GrammarSymbol, Set<FirstSetMember>> firstSetBase) {
        int i = 0;
        for (; i < chain.size(); i++) {
            Set<FirstSetMember> firstSetMembers = firstSetBase.get(chain.get(i));
            Set<FirstSetMember> newMembers = new HashSet<>(firstSetMembers);
            newMembers.remove(Epsilon.getInstance());
            result.addAll(newMembers);
            if (!firstSetMembers.contains(Epsilon.getInstance())) {
                break;
            }
        }
        if (i == chain.size()) {
            result.add(Epsilon.getInstance());
        }
    }
}
