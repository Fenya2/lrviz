package ru.urfu.lrviz.core.lr.operations;

import ru.urfu.lrviz.core.grammar.GrammarSymbol;
import ru.urfu.lrviz.core.lr.LR0AutomatonState;

import static ru.urfu.lrviz.core.lr.operations.OperationLevel.ACTION;

public final class AddTransitionOperation extends BuildOperation {

    public final LR0AutomatonState from;
    public final LR0AutomatonState to;
    public final GrammarSymbol through;

    public AddTransitionOperation(LR0AutomatonState from, LR0AutomatonState to, GrammarSymbol through) {
        super("Добавляем переход из состояния '%s' в состояние '%s' по символу '%s'"
                .formatted(from.name(), to.name(), through.lexicalValue), ACTION);
        this.from = from;
        this.to = to;
        this.through = through;
    }
}
