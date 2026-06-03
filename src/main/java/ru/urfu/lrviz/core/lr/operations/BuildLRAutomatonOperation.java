package ru.urfu.lrviz.core.lr.operations;

import ru.urfu.lrviz.core.lr.AutomatonType;

import static ru.urfu.lrviz.core.lr.operations.OperationLevel.COMMENT;

/**
 * Операция начала построения LR-автомата
 *
 * @author fenya
 * @since 15.03.2026
 */
public class BuildLRAutomatonOperation extends BuildOperation {
    private final AutomatonType automatonType;

    public BuildLRAutomatonOperation(AutomatonType automatonType) {
        super("Строим автомат %s".formatted(automatonType), COMMENT);
        this.automatonType = automatonType;
    }

    public AutomatonType getAutomatonType() {
        return automatonType;
    }
}
