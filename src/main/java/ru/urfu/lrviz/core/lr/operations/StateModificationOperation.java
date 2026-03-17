package ru.urfu.lrviz.core.lr.operations;

/**
 *
 * @author fenya
 * @since 17.03.2026
 */
public abstract class StateModificationOperation extends ActionOperation {
    public final String stateName;

    protected StateModificationOperation(String message, String stateName) {
        super(message);
        this.stateName = stateName;
    }
}
