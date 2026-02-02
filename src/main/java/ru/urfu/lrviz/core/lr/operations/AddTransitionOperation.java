package ru.urfu.lrviz.core.lr.operations;

import ru.urfu.lrviz.core.grammar.GrammarSymbol;

import static ru.urfu.lrviz.core.lr.operations.OperationLevel.ACTION;

public final class AddTransitionOperation extends BuildOperation {

    public final String from;
    public final String to;
    public final GrammarSymbol through;

    public AddTransitionOperation(String from, String to, GrammarSymbol through) {
        super("Добавляем переход из состояния '%s' в состояние '%s' по символу '%s'"
                .formatted(from, to, through.lexicalValue), ACTION);
        this.from = from;
        this.to = to;
        this.through = through;
    }
}
