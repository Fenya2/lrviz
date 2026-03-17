package ru.urfu.lrviz.core.lr.operations;

import ru.urfu.lrviz.core.grammar.GrammarSymbol;
import ru.urfu.lrviz.core.lr.TransitionSymbol;

public final class AddTransitionOperation extends TransitionModificationOperation {
    public AddTransitionOperation(String from, String to, TransitionSymbol through) {
        String message = "Добавляем переход из состояния '%s' в состояние '%s' по символу '%s'."
                .formatted(from, to, through.asString());
        super(message, from, to, through);
    }
}
