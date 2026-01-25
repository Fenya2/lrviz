package ru.urfu.lrviz.core.lr.operations;

import ru.urfu.lrviz.core.grammar.GrammarSymbol;

import static ru.urfu.lrviz.core.lr.operations.OperationLevel.COMMENT;

public class CheckForNewStateOperation extends BuildOperation {
    public CheckForNewStateOperation(GrammarSymbol transitionSymbol) {
        super("Проверяем, нужно ли вводить новое состояние при переходе по %s".formatted(transitionSymbol.lexicalValue), COMMENT);
    }
}
