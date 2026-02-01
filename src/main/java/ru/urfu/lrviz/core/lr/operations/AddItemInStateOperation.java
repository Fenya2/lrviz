package ru.urfu.lrviz.core.lr.operations;

import ru.urfu.lrviz.core.lr.LR0AutomatonState;
import ru.urfu.lrviz.core.lr.LR0Item;

import static ru.urfu.lrviz.core.lr.operations.OperationLevel.ACTION;

public final class AddItemInStateOperation extends BuildOperation {

    public final LR0AutomatonState state;
    public final LR0Item item;

    public AddItemInStateOperation(LR0AutomatonState state, LR0Item item) {
        super("Добавляем в состояние '%s' LR(0)-пункт '%s'.".formatted(state.getName(), item), ACTION);
        this.state = state;
        this.item = item;
    }
}
