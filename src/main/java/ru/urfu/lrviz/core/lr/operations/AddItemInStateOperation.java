package ru.urfu.lrviz.core.lr.operations;

import ru.urfu.lrviz.core.lr.lrnew.LRItem;

import static ru.urfu.lrviz.core.lr.operations.OperationLevel.ACTION;

public final class AddItemInStateOperation extends BuildOperation {

    public final String stateName;
    public final LRItem item;

    public AddItemInStateOperation(String stateName, LRItem item) {
        super("Добавляем в состояние '%s' LR(0)-пункт '%s'.".formatted(stateName, item), ACTION);
        this.stateName = stateName;
        this.item = item;
    }
}
