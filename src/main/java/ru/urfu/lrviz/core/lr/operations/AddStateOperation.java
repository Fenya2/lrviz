package ru.urfu.lrviz.core.lr.operations;

import static ru.urfu.lrviz.core.lr.operations.OperationLevel.ACTION;

public final class AddStateOperation extends BuildOperation {

    public AddStateOperation(String stateName) {
        super("Добавляем состояние '%s'".formatted(stateName), ACTION);
    }
}
