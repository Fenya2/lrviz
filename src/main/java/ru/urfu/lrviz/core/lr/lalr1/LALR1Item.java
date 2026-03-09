package ru.urfu.lrviz.core.lr.lalr1;

import ru.urfu.lrviz.core.grammar.Rule;
import ru.urfu.lrviz.core.lr.LRItem;
import ru.urfu.lrviz.core.lr.TransitionSymbol;
import ru.urfu.lrviz.core.lr.lr1.LR1Item;
import ru.urfu.lrviz.core.lr.lr1.LookAheadSymbol;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author fenya
 * @since 09.03.2026
 */
public class LALR1Item extends LRItem {
    private final Set<LookAheadSymbol> lookAheadSymbols;

    public LALR1Item(Rule rule, int dotIndex, Set<LookAheadSymbol> lookAheadSymbols) {
        super(rule, dotIndex);
        this.lookAheadSymbols = lookAheadSymbols;
    }

    @Override
    public LRItem shift() {
        return new LALR1Item(getRule(), getDotIndex() + 1, lookAheadSymbols);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (LALR1Item) obj;
        return Objects.equals(this.getRule(), that.getRule())
                && this.getDotIndex() == that.getDotIndex()
                && Objects.equals(this.lookAheadSymbols, that.lookAheadSymbols);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getRule(), this.getDotIndex());
    }

    @Override
    public String asString() {
        String lookAheads = lookAheadSymbols.stream().map(TransitionSymbol::asString).collect(Collectors.joining(","));
        return "[" + ruleToString() + "; " + lookAheads + "]";
    }

    public static LR1Item ofInitial(Rule rule, LookAheadSymbol lookAheadSymbol) {
        return new LR1Item(rule, 0, lookAheadSymbol);
    }

    public Set<LookAheadSymbol> getLookAheadSymbols() {
        return lookAheadSymbols;
    }
}
