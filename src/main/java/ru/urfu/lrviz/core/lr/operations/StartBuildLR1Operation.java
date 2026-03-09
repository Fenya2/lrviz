package ru.urfu.lrviz.core.lr.operations;

import static ru.urfu.lrviz.core.lr.operations.OperationLevel.COMMENT;

/**
 *
 * @author fenya
 * @since 10.03.2026
 */
public class StartBuildLR1Operation extends BuildOperation {
    public StartBuildLR1Operation() {
        super("Строим LR(1)-автомат", COMMENT);
    }
}
