package ru.urfu.lrviz.core.lr.operations;

import ru.urfu.lrviz.core.lr.lalr1.LALR1Item;
import ru.urfu.lrviz.core.lr.lr1.LookAheadSymbol;

/**
 * Операция добавления символа предпросмотра в LALR(1)-пункт
 *
 * @author fenya
 * @since 30.03.2026
 */
public class AddLookAheadOperation extends ActionOperation {
    public final String stateName;
    public final LALR1Item item;
    public final LookAheadSymbol lookAheadSymbol;

    public AddLookAheadOperation(String stateName, LALR1Item item, LookAheadSymbol lookAheadSymbol) {
        super("Добавляем символ предпросмотра '%s' в пункт '%s' состояния '%s'".formatted(lookAheadSymbol.asString(), item.asString(), stateName));
        this.item = item;
        this.lookAheadSymbol = lookAheadSymbol;
        this.stateName = stateName;
    }
}
