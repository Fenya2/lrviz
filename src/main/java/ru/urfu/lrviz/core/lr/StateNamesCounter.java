package ru.urfu.lrviz.core.lr;

import java.util.HashMap;
import java.util.Map;

/**
 * Генератор имен состояний автомата
 */
public class StateNamesCounter {
    private final Map<String, Integer> stateCount;

    public StateNamesCounter() {
        this.stateCount = new HashMap<>();
    }

    public int nextNumber(String stateName) {
        if (!stateCount.containsKey(stateName)) {
            stateCount.put(stateName, 2);
            return 1;
        }
        int result = stateCount.get(stateName);
        stateCount.put(stateName, result + 1);
        return result;
    }
}
