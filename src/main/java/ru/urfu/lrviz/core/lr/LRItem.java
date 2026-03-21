package ru.urfu.lrviz.core.lr;

import ru.urfu.lrviz.core.grammar.Rule;
import ru.urfu.lrviz.core.lr.lr1.EndOfChainSymbol;

/**
 * LR-пункт
 *
 * @author fenya
 * @since 01.02.2026
 */
public abstract class LRItem {
    private final Rule rule;
    private final int dotIndex;

    protected LRItem(Rule rule, int dotIndex) {
        checkIndexIsValidForRule(rule, dotIndex);
        this.rule = rule;
        this.dotIndex = dotIndex;
    }

    private void checkIndexIsValidForRule(Rule rule, int dotIndex) {
        if (dotIndex < 0 || dotIndex > rule.right().size()) {
            throw new IllegalArgumentException(
                    "DotIndex %d is not valid for rule %s".formatted(dotIndex, rule));
        }
    }

    public Rule getRule() {
        return rule;
    }

    /**
     * @return символ, следующий за точкой. Если за точкой ничего нет, возвращает {@link EndOfChainSymbol#getInstance()}
     */
    public TransitionSymbol getDotSymbol() {
        if (isFinal()) {
            return EndOfChainSymbol.getInstance();
        }
        return getRule().right().get(dotIndex);
    }

    public int getDotIndex() {
        return dotIndex;
    }

    public boolean isDotSymbolAtTheBeginning() {
        return dotIndex == 0;
    }

    public boolean isFinal() {
        return dotIndex == rule.right().size();
    }

    public abstract LRItem shift();

    public abstract String asString();

    @Override
    public String toString() {
        return asString();
    }

    protected final String ruleToString() {
        return rule.left() + "→"
                + String.join("", rule.right().subList(0, dotIndex).stream().map(grammarSymbol -> grammarSymbol.lexicalValue).toList())
                + "•"
                + String.join("", rule.right().subList(dotIndex, rule.right().size()).stream().map(grammarSymbol -> grammarSymbol.lexicalValue).toList());
    }
}
