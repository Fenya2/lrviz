package ru.urfu.lrviz.core.lr.operations;

import static ru.urfu.lrviz.core.lr.operations.OperationLevel.COMMENT;

public final class ExtendGrammarOperation extends BuildOperation {
    public ExtendGrammarOperation() {
        super("Расширяем исходную грамматику.", COMMENT);
    }
}
