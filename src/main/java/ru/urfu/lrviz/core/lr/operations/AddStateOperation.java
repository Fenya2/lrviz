package ru.urfu.lrviz.core.lr.operations;

import ru.urfu.lrviz.core.lr.LR0AutomatonState;

import static ru.urfu.lrviz.core.lr.operations.OperationLevel.ACTION;

public final class AddStateOperation extends BuildOperation {

    public final LR0AutomatonState state;

    public AddStateOperation(LR0AutomatonState state) {
        super("Добавляем состояние '%s'".formatted(state.name()), ACTION);
        this.state = state;
    }
}
