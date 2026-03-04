package ru.urfu.lrviz.core.lr;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Состояние {@link LRAutomaton lr-автомата}
 *
 * @author fenya
 * @since 01.02.2026
 */
public record LRState(Set<LRItem> items) {

    public LRState(LRItem... items) {
        this(Set.copyOf(Arrays.asList(items)));
    }

    public static Set<LRItem> diff(LRState before, LRState after) {
        HashSet<LRItem> diff = new HashSet<>(after.items);
        diff.removeAll(before.items);
        return diff;
    }

    @Override
    public String toString() {
        return items.toString();
    }
}
