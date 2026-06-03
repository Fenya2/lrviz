package ru.urfu.lrviz.core.lr.operations;

/**
 * Операция удаления состояния LR-автомата
 *
 * @author fenya
 * @since 17.03.2026
 */
public class DeleteStateOperation extends StateModificationOperation {
    public DeleteStateOperation(String stateName) {
        super("Удаляем состояние '%s'.".formatted(stateName), stateName);
    }
}
