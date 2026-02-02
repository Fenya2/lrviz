package ru.urfu.lrviz.core.lr;

import ru.urfu.lrviz.core.lr.operations.BuildOperation;

import java.util.ArrayList;
import java.util.List;

public class BuildLog {
    private final List<BuildOperation> operations;

    public BuildLog() {
        this.operations = new ArrayList<>();
    }

    public void append(BuildOperation operation) {
        operations.add(operation);
    }

    public List<BuildOperation> getOperations() {
        return operations;
    }
}
