package ru.urfu.lrviz.core.lr.operations;

/**
 * Операция построения LR-автомата
 *
 * @author fenya
 * @since 28.03.2026
 */
public abstract class BuildOperation {
    public final String message;
    public final OperationLevel level;


    BuildOperation(String message, OperationLevel level) {
        this.message = message;
        this.level = level;
    }

    @Override
    public String toString() {
        return message;
    }
}
