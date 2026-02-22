package ru.urfu.lrviz.core.grammar;

import ru.urfu.lrviz.core.lr.lr1.LookAheadSymbol;

public final class Terminal extends GrammarSymbol implements LookAheadSymbol, FirstSetMember {
    public Terminal(String lexicalValue) {
        super(lexicalValue);
    }
}