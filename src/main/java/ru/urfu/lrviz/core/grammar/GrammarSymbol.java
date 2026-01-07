package ru.urfu.lrviz.core.grammar;

import java.util.Objects;

public abstract sealed class GrammarSymbol permits Terminal, NonTerminal {
    public final String lexicalValue;

    public GrammarSymbol(String lexicalValue) {
        this.lexicalValue = lexicalValue;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GrammarSymbol that = (GrammarSymbol) o;
        return Objects.equals(lexicalValue, that.lexicalValue);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(lexicalValue);
    }

    @Override
    public String toString() {
        return lexicalValue;
    }
}
