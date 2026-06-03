package ru.urfu.lrviz.core.lr.operations;

/**
 * Операция добавления состояния в LR-автомат
 * @author fenya
 * @since 28.03.2026
 */
public final class AddStateOperation extends StateModificationOperation {

    public AddStateOperation(String stateName) {
        super("Добавляем состояние '%s'.".formatted(stateName), stateName);
    }
}
