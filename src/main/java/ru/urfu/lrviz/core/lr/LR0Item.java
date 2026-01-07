package ru.urfu.lrviz.core.lr;

import ru.urfu.lrviz.core.grammar.Rule;

public record LR0Item(Rule rule, int dotIndex) {
    public static LR0Item ofInitial(Rule rule) {
        return new LR0Item(rule, 0);
    }

    public LR0Item {
        checkSeparatorIsValidForRule(dotIndex, rule);
    }

    private void checkSeparatorIsValidForRule(int separator, Rule rule) {
        if (separator < 0 || separator > rule.right().size()) {
            throw new IllegalArgumentException(
                    "DotIndex %d is not valid for rule %s".formatted(separator, rule));
        }
    }

    @Override
    public String toString() {
        return rule.left() + "→"
                + String.join("", rule.right().subList(0, dotIndex).stream().map(grammarSymbol -> grammarSymbol.lexicalValue).toList())
                + "•"
                + String.join("", rule.right().subList(dotIndex, rule.right().size()).stream().map(grammarSymbol -> grammarSymbol.lexicalValue).toList());
    }

    public boolean isFinal() {
        return dotIndex == rule.right().size();
    }
}