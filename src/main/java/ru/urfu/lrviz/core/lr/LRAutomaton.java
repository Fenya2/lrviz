package ru.urfu.lrviz.core.lr;

import java.util.Map;

/**
 * LR автомат. Состояния состоят из LR(k) пунктовю Не содержит заключительных состояний
 *
 * @author fenya
 * @since 01.02.2026
 */
public record LRAutomaton(Map<String, LRState> namedStates, Map<TransitionKey, String> transitionMap) {
    public record TransitionKey(String stateName, TransitionSymbol symbol) {
        @Override
        public String toString() {
            return "(" + stateName + ", " + symbol.asString() + ")";
        }
    }
}
