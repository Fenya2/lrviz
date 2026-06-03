package ru.urfu.lrviz.core.lr.operations;

import ru.urfu.lrviz.core.lr.TransitionSymbol;

/**
 * Операция удаления перехода в LR-автомате
 * @author fenya
 * @since 17.03.2026
 */
public class DeleteTransitionOperation extends TransitionModificationOperation {
    public DeleteTransitionOperation(String from, String to, TransitionSymbol through) {
        String message = "Удаляем переход из состояния '%s' в состояние '%s' по символу '%s'.".formatted(from, to, through.asString());
        super(message, from, to, through);
    }
}
