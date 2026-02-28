package ru.urfu.lrviz.core.lr.lr1;

import ru.urfu.lrviz.core.grammar.Rule;
import ru.urfu.lrviz.core.lr.LRItem;

import java.util.Objects;

/**
 * @author fenya
 * @since 14.02.2026
 */
public class LR1Item extends LRItem {
    private final LookAheadSymbol lookAheadSymbol;

    public LR1Item(Rule rule, int dotIndex, LookAheadSymbol lookAheadSymbol) {
        super(rule, dotIndex);
        this.lookAheadSymbol = lookAheadSymbol;
    }

    public static LR1Item ofInitial(Rule rule, LookAheadSymbol lookAheadSymbol) {
        return new LR1Item(rule, 0, lookAheadSymbol);
    }

    @Override
    public LRItem shift() {
        return new LR1Item(getRule(), getDotIndex() + 1, lookAheadSymbol);
    }

    public LookAheadSymbol getLookAheadSymbol() {
        return lookAheadSymbol;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (LR1Item) obj;
        return Objects.equals(this.getRule(), that.getRule())
                && this.getDotIndex() == that.getDotIndex()
                && Objects.equals(this.lookAheadSymbol, that.lookAheadSymbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getRule(), this.getDotIndex());
    }

    @Override
    public String asString() {
        return "[" + ruleToString() + ", " + lookAheadSymbol.asString() + "]";
    }
}
