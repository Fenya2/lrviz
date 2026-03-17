package ru.urfu.lrviz.core.lr.operations;

/**
 * @author fenya
 * @since 17.03.2026
 */
public class DeleteStateOperation extends StateModificationOperation {
    public DeleteStateOperation(String stateName) {
        super("Удаляем состояние '%s'.".formatted(stateName), stateName);
    }
}
