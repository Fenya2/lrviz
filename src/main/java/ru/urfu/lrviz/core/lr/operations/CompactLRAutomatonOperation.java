package ru.urfu.lrviz.core.lr.operations;

import static ru.urfu.lrviz.core.lr.operations.OperationLevel.COMMENT;

/**
 *
 * @author fenya
 * @since 15.03.2026
 */
public class CompactLRAutomatonOperation extends BuildOperation {
    public CompactLRAutomatonOperation() {
        super("Уплотняем полученный LR-автомат", COMMENT);
    }
}
