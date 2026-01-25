package ru.urfu.lrviz.core.lr.operations;

import ru.urfu.lrviz.core.lr.LR0AutomatonState;

import static ru.urfu.lrviz.core.lr.operations.OperationLevel.COMMENT;

public class ConfirmStateAlreadyExist extends BuildOperation {
    public ConfirmStateAlreadyExist(LR0AutomatonState state) {
        super("Такое состояние уже есть в автомате ('%s'), поэтому добавляем переход к нему".formatted(state), COMMENT);
    }
}
