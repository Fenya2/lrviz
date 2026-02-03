package ru.urfu.lrviz.core.lr.operations;

import static ru.urfu.lrviz.core.lr.operations.OperationLevel.COMMENT;

public class StartAddNewTransitionsOperation extends BuildOperation {
    public StartAddNewTransitionsOperation(String stateName) {
        super("Просматриваем состояние '%s'".formatted(stateName), COMMENT);
    }
}
