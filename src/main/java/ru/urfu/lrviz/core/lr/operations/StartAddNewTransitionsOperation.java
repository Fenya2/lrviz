package ru.urfu.lrviz.core.lr.operations;

import static ru.urfu.lrviz.core.lr.operations.OperationLevel.COMMENT;

/**
 * Операция начала добавления переходов при обработке очередного состояния при построении LR-автоматов
 *
 * @author fenya
 * @since 28.03.2026
 */
public class StartAddNewTransitionsOperation extends BuildOperation {
    public StartAddNewTransitionsOperation(String stateName) {
        super("Просматриваем состояние '%s'".formatted(stateName), COMMENT);
    }
}
