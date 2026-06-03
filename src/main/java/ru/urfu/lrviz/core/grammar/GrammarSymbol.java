package ru.urfu.lrviz.core.grammar;

import ru.urfu.lrviz.core.lr.TransitionSymbol;

import java.util.Objects;

public abstract sealed class GrammarSymbol implements TransitionSymbol permits Terminal, NonTerminal {
    public final String lexicalValue;

    protected GrammarSymbol(String lexicalValue) {
        this.lexicalValue = lexicalValue;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GrammarSymbol that = (GrammarSymbol) o;
        return Objects.equals(lexicalValue, that.lexicalValue);
    }

    @Override
    public String asString() {
        return lexicalValue;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(lexicalValue);
    }

    @Override
    public String toString() {
        return asString();
    }
}
