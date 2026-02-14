package ru.urfu.lrviz.core.lr.lr0;

import ru.urfu.lrviz.core.grammar.Rule;
import ru.urfu.lrviz.core.lr.LRItem;

import java.util.Objects;

public final class LR0Item extends LRItem {
    public LR0Item(Rule rule, int dotIndex) {
        super(rule, dotIndex);
    }

    public static LR0Item ofInitial(Rule rule) {
        return new LR0Item(rule, 0);
    }

    public static LR0Item ofFinal(Rule rule) {
        return new LR0Item(rule, rule.right().size());
    }

    public LR0Item shift() {
        return new LR0Item(getRule(), getDotIndex() + 1);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (LR0Item) obj;
        return Objects.equals(this.getRule(), that.getRule())
                && this.getDotIndex() == that.getDotIndex();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getRule(), this.getDotIndex());
    }
}