package ru.urfu.lrviz.core.lr.operations;

/**
 *
 * @author fenya
 * @since 17.03.2026
 */
public class ActionOperation extends BuildOperation {
    ActionOperation(String message) {
        super(message, OperationLevel.ACTION);
    }
}
