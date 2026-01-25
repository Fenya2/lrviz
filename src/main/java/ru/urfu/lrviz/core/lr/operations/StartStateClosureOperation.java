package ru.urfu.lrviz.core.lr.operations;

import ru.urfu.lrviz.core.lr.LR0AutomatonState;

import static ru.urfu.lrviz.core.lr.operations.OperationLevel.COMMENT;

public final class StartStateClosureOperation extends BuildOperation {

    public StartStateClosureOperation(LR0AutomatonState closingState) {
        super("Замыкаем состояние '%s'.".formatted(closingState.name()), COMMENT);
    }
}
