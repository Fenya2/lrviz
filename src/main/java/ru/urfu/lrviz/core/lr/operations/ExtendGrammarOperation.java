package ru.urfu.lrviz.core.lr.operations;

import static ru.urfu.lrviz.core.lr.operations.OperationLevel.COMMENT;

/**
 * Операция расширения грамматики
 *
 * @author fenya
 * @since 28.03.2026
 */
public final class ExtendGrammarOperation extends BuildOperation {
    public ExtendGrammarOperation() {
        super("Расширяем исходную грамматику.", COMMENT);
    }
}
