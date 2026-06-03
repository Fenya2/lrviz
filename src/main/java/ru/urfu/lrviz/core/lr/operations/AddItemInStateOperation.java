package ru.urfu.lrviz.core.lr.operations;

import ru.urfu.lrviz.core.lr.LRItem;

/**
 * Операция добавления LR-пункта в состояние LR-автомата
 *
 * @author fenya
 * @since 28.03.2026
 */
public final class AddItemInStateOperation extends ActionOperation {

    public final String stateName;
    public final LRItem item;

    public AddItemInStateOperation(String stateName, LRItem item) {
        super("Добавляем в состояние '%s' пункт '%s'.".formatted(stateName, item));
        this.stateName = stateName;
        this.item = item;
    }
}
