package ru.urfu.lrviz.core.lr.operations;

import static ru.urfu.lrviz.core.lr.operations.OperationLevel.COMMENT;

public class StartAddNewTransitions extends BuildOperation {
    public StartAddNewTransitions(String stateName) {
        super("Пробуем добавить переходы из состояния '%s'".formatted(stateName), COMMENT);
    }
}
