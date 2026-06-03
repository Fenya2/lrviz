package ru.urfu.lrviz.core.grammar;

/**
 * @author fenya
 * @since 15.02.2026
 */
public class Epsilon implements FirstSetMember {

    private static final Epsilon instance = new Epsilon();

    public static Epsilon getInstance() {
        return instance;
    }

    private Epsilon() {
    }

    @Override
    public String toString() {
        return "ε";
    }
}
