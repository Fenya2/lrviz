package ru.urfu.lrviz.core.lr.operations;

import ru.urfu.lrviz.core.lr.TransitionSymbol;

/**
 * Операция добавления перехода в LR-автомате
 *
 * @author fenya
 * @since 28.03.2026
 */
public final class AddTransitionOperation extends TransitionModificationOperation {
    public AddTransitionOperation(String from, String to, TransitionSymbol through) {
        String message = "Добавляем переход из состояния '%s' в состояние '%s' по символу '%s'."
                .formatted(from, to, through.asString());
        super(message, from, to, through);
    }
}
