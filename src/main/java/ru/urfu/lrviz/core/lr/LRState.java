package ru.urfu.lrviz.core.lr;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
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

    /**
     * @return разность множества пунктов в состояниях {@code after} и {@code before}
     */
    public static Set<LRItem> diff(LRState before, LRState after) {
        HashSet<LRItem> diff = new LinkedHashSet<>(after.items);
        diff.removeAll(before.items);
        return diff;
    }

    /**
     * @return состояние с множеством пунктов, равным объединению множеств пунктов из {@code state1} и {@code state2}
     */
    public static LRState merge(LRState state1, LRState state2) {
        HashSet<LRItem> newItems = new LinkedHashSet<>(state1.items);
        newItems.addAll(state2.items);
        return new LRState(newItems);
    }

    @Override
    public String toString() {
        return items.toString();
    }
}
