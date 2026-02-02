package ru.urfu.lrviz.core.lr.operations;

import static ru.urfu.lrviz.core.lr.operations.OperationLevel.COMMENT;

public final class StartStateClosureOperation extends BuildOperation {

    public StartStateClosureOperation(String stateName) {
        super("Замыкаем состояние '%s'.".formatted(stateName), COMMENT);
    }
}
