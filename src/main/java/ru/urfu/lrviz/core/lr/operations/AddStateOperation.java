package ru.urfu.lrviz.core.lr.operations;

public final class AddStateOperation extends StateModificationOperation {

    public AddStateOperation(String stateName) {
        super("Добавляем состояние '%s'.".formatted(stateName), stateName);
    }
}
