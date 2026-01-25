package ru.urfu.lrviz.core.lr;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Генератор имен состояний автомата
 */
@Service
public class StateNamesCounter {
    private final ScopedValue<Map<String, Integer>> nextNumbers = ScopedValue.newInstance();

    public void execute(Runnable runnable) {
        ScopedValue.where(nextNumbers, new HashMap<>()).run(runnable);
    }

    public int nextNumber(String stateName) {
        Map<String, Integer> sessionMap = nextNumbers.get();
        if (!sessionMap.containsKey(stateName)) {
            sessionMap.put(stateName, 2);
            return 1;
        }
        int result = sessionMap.get(stateName);
        sessionMap.put(stateName, result + 1);
        return result;
    }
}
