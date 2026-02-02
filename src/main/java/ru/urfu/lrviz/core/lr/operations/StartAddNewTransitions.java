package ru.urfu.lrviz.core.lr.operations;

import static ru.urfu.lrviz.core.lr.operations.OperationLevel.COMMENT;

public class StartAddNewTransitions extends BuildOperation {
    public StartAddNewTransitions(String stateName) {
        super("Просматриваем состояние '%s'".formatted(stateName), COMMENT);
    }
}
