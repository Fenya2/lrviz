package ru.urfu.lrviz.core.lr.operations;

import static ru.urfu.lrviz.core.lr.operations.OperationLevel.COMMENT;

public class ConfirmNeedNewStateOperation extends BuildOperation {
    public ConfirmNeedNewStateOperation() {
        super("Такого состояния еще нет. Поэтому добавляем его", COMMENT);
    }
}
