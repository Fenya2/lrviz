package ru.urfu.lrviz.core.lr.lalr1;

import ru.urfu.lrviz.core.grammar.Rule;
import ru.urfu.lrviz.core.lr.LRItem;
import ru.urfu.lrviz.core.lr.TransitionSymbol;
import ru.urfu.lrviz.core.lr.lr0.LR0Item;
import ru.urfu.lrviz.core.lr.lr1.LR1Item;
import ru.urfu.lrviz.core.lr.lr1.LookAheadSymbol;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * LR(0)-пункт, связанный с множеством символов предпросмотра. Удобен при построении LALR(1)-ядер в алгоритме
 * {@link LALR1BuildAlgorithm#CHANNEL}
 *
 * @author fenya
 * @since 09.03.2026
 */
public class LALR1Item extends LRItem {
    private final Set<LookAheadSymbol> lookAheadSymbols;

    private LALR1Item(Rule rule, int dotIndex, Set<LookAheadSymbol> lookAheadSymbols) {
        super(rule, dotIndex);
        this.lookAheadSymbols = lookAheadSymbols;
    }

    /**
     * Создает пункт на основе LR(0)-пункта
     */
    public static LALR1Item fromLr0Item(LR0Item baseItem) {
        return new LALR1Item(baseItem.getRule(), baseItem.getDotIndex(), new LinkedHashSet<>());
    }

    /**
     * Создает пункт на основе LR(1)-пункта, добавляя в множество символов предпросмотра символ предпросмотра
     * переданного пункта
     */
    public static LALR1Item fromLr1Item(LR1Item baseItem) {
        LALR1Item lalr1Item = new LALR1Item(baseItem.getRule(), baseItem.getDotIndex(), new LinkedHashSet<>());
        lalr1Item.addLookAhead(baseItem.getLookAheadSymbol());
        return lalr1Item;
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
        return Objects.equals(getRule(), that.getRule()) && Objects.equals(getDotIndex(), that.getDotIndex());
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

    /**
     * @return символы предпросмотра
     */
    public Set<LookAheadSymbol> getLookAheadSymbols() {
        return lookAheadSymbols;
    }

    /**
     * Добавляет символ предпросмотра
     * @return {@code true}, если добавленного символа не было в множестве связанных символов предпросмотра
     */
    public boolean addLookAhead(LookAheadSymbol symbol) {
        return lookAheadSymbols.add(symbol);
    }
}
