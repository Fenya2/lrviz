package ru.urfu.lrviz.core.lr;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public record LR0AutomatonState(String name, Set<LR0Item> items) implements LRAutomatonState {

    public LR0AutomatonState(String name, Set<LR0Item> items) {
        this.name = name;
        this.items = new HashSet<>(items);
    }

    public void addItems(Set<LR0Item> newItems) {
        items.addAll(newItems);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LR0AutomatonState that = (LR0AutomatonState) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "(" + name + ", " + items + ")";
    }
}
