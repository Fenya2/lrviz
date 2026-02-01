package ru.urfu.lrviz.core.lr;

import ru.urfu.lrviz.core.grammar.Rule;
import ru.urfu.lrviz.core.lr.lrnew.LRItem;

import java.util.Objects;

public final class LR0Item implements LRItem {
    private final Rule rule;
    private final int dotIndex;

    public static LR0Item ofInitial(Rule rule) {
        return new LR0Item(rule, 0);
    }

    public LR0Item(Rule rule, int dotIndex) {
        checkSeparatorIsValidForRule(dotIndex, rule);
        this.rule = rule;
        this.dotIndex = dotIndex;
    }

    @Override
    public Rule getRule() {
        return rule;
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


    public int dotIndex() {
        return dotIndex;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (LR0Item) obj;
        return Objects.equals(this.rule, that.rule) &&
                this.dotIndex == that.dotIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rule, dotIndex);
    }

}