package ru.urfu.lrviz.core.lr.operations;

import ru.urfu.lrviz.core.grammar.GrammarSymbol;

import static ru.urfu.lrviz.core.lr.operations.OperationLevel.COMMENT;

public class CheckForNewStateOperation extends BuildOperation {
    public CheckForNewStateOperation(GrammarSymbol transitionSymbol, String fromStateName) {
        super("Проверяем, нужно ли вводить новое состояние при переходе по '%s' из '%s'".formatted(transitionSymbol.lexicalValue, fromStateName), COMMENT);
    }
}
