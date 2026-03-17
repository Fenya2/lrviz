package ru.urfu.lrviz.core.lr.operations;

import ru.urfu.lrviz.core.lr.TransitionSymbol;

/**
 *
 * @author fenya
 * @since 17.03.2026
 */
public abstract class TransitionModificationOperation extends ActionOperation {
    public final String from;
    public final String to;
    public final TransitionSymbol through;

    protected TransitionModificationOperation(String message, String from, String to, TransitionSymbol through) {
        super(message);
        this.from = from;
        this.to = to;
        this.through = through;
    }
}
