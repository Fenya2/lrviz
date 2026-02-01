package ru.urfu.lrviz.core.lr.operations;

import ru.urfu.lrviz.core.lr.LR0AutomatonState;

import static ru.urfu.lrviz.core.lr.operations.OperationLevel.COMMENT;

public class StartAddNewTransitions extends BuildOperation {
    public StartAddNewTransitions(LR0AutomatonState from) {
        super("Пробуем добавить переходы из состояния '%s'".formatted(from.getName()), COMMENT);
    }
}
