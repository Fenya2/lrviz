package ru.urfu.lrviz.core.lr;

import org.junit.jupiter.api.Test;
import ru.urfu.lrviz.core.grammar.NonTerminal;
import ru.urfu.lrviz.core.grammar.Rule;
import ru.urfu.lrviz.core.grammar.Terminal;
import ru.urfu.lrviz.core.lr.lalr1.LALR1Item;
import ru.urfu.lrviz.core.lr.lr0.LR0Item;
import ru.urfu.lrviz.core.lr.lr1.EndOfChainSymbol;
import ru.urfu.lrviz.core.lr.lr1.LR1Item;
import ru.urfu.lrviz.core.lr.operations.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты на {@link LRAutomatonReconstructor}
 *
 * @author fenya
 * @since 30.03.2026
 */
@SuppressWarnings("java:S117") // имена переменных здесь оправданы
class LRAutomatonReconstructorTest {

    private static final String STATE_NAME_1 = "state1";
    private static final String STATE_NAME_2 = "state2";

    private final LRAutomatonReconstructor reconstructor = new LRAutomatonReconstructor();

    @Test
    void reconstructAddState() {
        BuildLog buildLog = new BuildLog();
        buildLog.append(new AddStateOperation(STATE_NAME_1));

        LRAutomaton automaton = reconstructor.reconstruct(buildLog);

        assertTrue(automaton.namedStates().containsKey(STATE_NAME_1));
        assertTrue(automaton.namedStates().get(STATE_NAME_1).items().isEmpty());
        assertTrue(automaton.transitions().isEmpty());
    }

    @Test
    void reconstructDeleteState() {
        BuildLog buildLog = new BuildLog();
        buildLog.append(new AddStateOperation(STATE_NAME_1));
        buildLog.append(new DeleteStateOperation(STATE_NAME_1));

        LRAutomaton automaton = reconstructor.reconstruct(buildLog);

        assertFalse(automaton.namedStates().containsKey(STATE_NAME_1));
        assertTrue(automaton.namedStates().isEmpty());
    }

    @Test
    void reconstructAddTransition() {
        BuildLog buildLog = new BuildLog();
        buildLog.append(new AddStateOperation(STATE_NAME_1));
        buildLog.append(new AddStateOperation(STATE_NAME_2));
        Terminal symbol = new Terminal("a");
        buildLog.append(new AddTransitionOperation(STATE_NAME_1, STATE_NAME_2, symbol));

        LRAutomaton automaton = reconstructor.reconstruct(buildLog);

        LRAutomaton.TransitionKey key = new LRAutomaton.TransitionKey(STATE_NAME_1, symbol);
        assertTrue(automaton.transitions().containsKey(key));
        assertEquals(STATE_NAME_2, automaton.transitions().get(key));
    }

    @Test
    void reconstructDeleteTransition() {
        BuildLog buildLog = new BuildLog();
        buildLog.append(new AddStateOperation(STATE_NAME_1));
        buildLog.append(new AddStateOperation(STATE_NAME_2));
        Terminal symbol = new Terminal("a");
        buildLog.append(new AddTransitionOperation(STATE_NAME_1, STATE_NAME_2, symbol));
        buildLog.append(new DeleteTransitionOperation(STATE_NAME_1, STATE_NAME_2, symbol));

        LRAutomaton automaton = reconstructor.reconstruct(buildLog);

        LRAutomaton.TransitionKey key = new LRAutomaton.TransitionKey(STATE_NAME_1, symbol);
        assertFalse(automaton.transitions().containsKey(key));
    }

    @Test
    void reconstructAddItemInState() {
        BuildLog buildLog = new BuildLog();
        buildLog.append(new AddStateOperation(STATE_NAME_1));
        NonTerminal S = new NonTerminal("S");
        Terminal a = new Terminal("a");
        Rule rule = new Rule(S, a);
        LR0Item item = new LR0Item(rule, 0);
        buildLog.append(new AddItemInStateOperation(STATE_NAME_1, item));

        LRAutomaton automaton = reconstructor.reconstruct(buildLog);

        LRState state = automaton.namedStates().get(STATE_NAME_1);
        assertEquals(1, state.items().size());
        assertTrue(state.items().contains(item));
    }

    @Test
    void reconstructAddItemInStateThrowsWhenStateNotAdded() {
        BuildLog buildLog = new BuildLog();
        NonTerminal S = new NonTerminal("S");
        Terminal a = new Terminal("a");
        Rule rule = new Rule(S, a);
        LR0Item item = new LR0Item(rule, 0);
        buildLog.append(new AddItemInStateOperation(STATE_NAME_1, item));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> reconstructor.reconstruct(buildLog));

        assertTrue(exception.getMessage().contains("State not added yet"));
    }

    @Test
    void reconstructAddLookAhead() {
        BuildLog buildLog = new BuildLog();
        buildLog.append(new AddStateOperation(STATE_NAME_1));

        NonTerminal S = new NonTerminal("S");
        Terminal a = new Terminal("a");
        Terminal b = new Terminal("b");
        Rule rule = new Rule(S, a);
        LR0Item baseItem = new LR0Item(rule, 0);
        buildLog.append(new AddItemInStateOperation(STATE_NAME_1, baseItem));

        LALR1Item item = LALR1Item.fromLr0Item(baseItem);
        buildLog.append(new AddLookAheadOperation(STATE_NAME_1, item, b));

        LRAutomaton automaton = reconstructor.reconstruct(buildLog);

        LRState state = automaton.namedStates().get(STATE_NAME_1);
        assertEquals(1, state.items().size());
        LALR1Item resultItem = (LALR1Item) state.items().iterator().next();
        assertEquals(1, resultItem.getLookAheadSymbols().size());
        assertTrue(resultItem.getLookAheadSymbols().contains(b));
    }

    @Test
    void reconstructAddLookAheadConvertsLr0Item() {
        BuildLog buildLog = new BuildLog();
        buildLog.append(new AddStateOperation(STATE_NAME_1));

        NonTerminal S = new NonTerminal("S");
        Terminal a = new Terminal("a");
        Terminal b = new Terminal("b");
        Rule rule = new Rule(S, a);
        LR0Item baseItem = new LR0Item(rule, 0);
        buildLog.append(new AddItemInStateOperation(STATE_NAME_1, baseItem));

        LALR1Item item = LALR1Item.fromLr0Item(baseItem);
        buildLog.append(new AddLookAheadOperation(STATE_NAME_1, item, b));

        LRAutomaton automaton = reconstructor.reconstruct(buildLog);

        LRState state = automaton.namedStates().get(STATE_NAME_1);
        LRItem resultItem = state.items().iterator().next();
        assertInstanceOf(LALR1Item.class, resultItem);
    }

    @Test
    void reconstructAddLookAheadConvertsLr1Item() {
        BuildLog buildLog = new BuildLog();
        buildLog.append(new AddStateOperation(STATE_NAME_1));

        NonTerminal S = new NonTerminal("S");
        Terminal a = new Terminal("a");
        Terminal b = new Terminal("b");
        Rule rule = new Rule(S, a);
        LR1Item baseItem = new LR1Item(rule, 0, b);
        buildLog.append(new AddItemInStateOperation(STATE_NAME_1, baseItem));

        LALR1Item item = LALR1Item.fromLr1Item(baseItem);
        Terminal c = new Terminal("c");
        buildLog.append(new AddLookAheadOperation(STATE_NAME_1, item, c));

        LRAutomaton automaton = reconstructor.reconstruct(buildLog);

        LRState state = automaton.namedStates().get(STATE_NAME_1);
        LALR1Item resultItem = (LALR1Item) state.items().iterator().next();
        assertEquals(2, resultItem.getLookAheadSymbols().size());
        assertTrue(resultItem.getLookAheadSymbols().contains(b));
        assertTrue(resultItem.getLookAheadSymbols().contains(c));
    }

    @Test
    void reconstructAddLookAheadAddsToExistingLalr1Item() {
        BuildLog buildLog = new BuildLog();
        buildLog.append(new AddStateOperation(STATE_NAME_1));

        NonTerminal S = new NonTerminal("S");
        Terminal a = new Terminal("a");
        Terminal b = new Terminal("b");
        Terminal c = new Terminal("c");
        Rule rule = new Rule(S, a);
        LALR1Item baseItem = LALR1Item.fromLr0Item(new LR0Item(rule, 0));
        baseItem.addLookAhead(b);
        buildLog.append(new AddItemInStateOperation(STATE_NAME_1, baseItem));

        buildLog.append(new AddLookAheadOperation(STATE_NAME_1, baseItem, c));

        LRAutomaton automaton = reconstructor.reconstruct(buildLog);

        LRState state = automaton.namedStates().get(STATE_NAME_1);
        LALR1Item resultItem = (LALR1Item) state.items().iterator().next();
        assertEquals(Set.of(b, c), resultItem.getLookAheadSymbols());
    }

    @Test
    void reconstructAddLookAheadThrowsWhenItemNotFound() {
        BuildLog buildLog = new BuildLog();
        buildLog.append(new AddStateOperation(STATE_NAME_1));

        NonTerminal S = new NonTerminal("S");
        Terminal a = new Terminal("a");
        Terminal b = new Terminal("b");
        Rule rule = new Rule(S, a);
        LALR1Item item = LALR1Item.fromLr0Item(new LR0Item(rule, 0));
        buildLog.append(new AddLookAheadOperation(STATE_NAME_1, item, b));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> reconstructor.reconstruct(buildLog));

        assertTrue(exception.getMessage().contains("Item not added yet"));
    }

    @Test
    void reconstructSkipsCommentOperations() {
        BuildLog buildLog = new BuildLog();
        buildLog.append(new AddStateOperation(STATE_NAME_1));
        buildLog.append(new ExtendGrammarOperation());
        buildLog.append(new DeleteStateOperation(STATE_NAME_1));

        LRAutomaton automaton = reconstructor.reconstruct(buildLog);

        assertFalse(automaton.namedStates().containsKey(STATE_NAME_1));
    }

    @Test
    void reconstructAddItemInStateMultipleItems() {
        BuildLog buildLog = new BuildLog();
        buildLog.append(new AddStateOperation(STATE_NAME_1));

        NonTerminal S = new NonTerminal("S");
        Terminal a = new Terminal("a");
        Terminal b = new Terminal("b");
        Rule rule1 = new Rule(S, a);
        Rule rule2 = new Rule(S, b);
        LR0Item item1 = new LR0Item(rule1, 0);
        LR0Item item2 = new LR0Item(rule2, 1);

        buildLog.append(new AddItemInStateOperation(STATE_NAME_1, item1));
        buildLog.append(new AddItemInStateOperation(STATE_NAME_1, item2));

        LRAutomaton automaton = reconstructor.reconstruct(buildLog);

        LRState state = automaton.namedStates().get(STATE_NAME_1);
        assertEquals(2, state.items().size());
        assertTrue(state.items().contains(item1));
        assertTrue(state.items().contains(item2));
    }

    @Test
    void reconstructEndOfChainSymbolAsLookAhead() {
        BuildLog buildLog = new BuildLog();
        buildLog.append(new AddStateOperation(STATE_NAME_1));

        NonTerminal S = new NonTerminal("S");
        Terminal a = new Terminal("a");
        Rule rule = new Rule(S, a);
        LR0Item baseItem = new LR0Item(rule, 1);
        buildLog.append(new AddItemInStateOperation(STATE_NAME_1, baseItem));

        LALR1Item item = LALR1Item.fromLr0Item(baseItem);
        buildLog.append(new AddLookAheadOperation(STATE_NAME_1, item, EndOfChainSymbol.getInstance()));

        LRAutomaton automaton = reconstructor.reconstruct(buildLog);

        LRState state = automaton.namedStates().get(STATE_NAME_1);
        LALR1Item resultItem = (LALR1Item) state.items().iterator().next();
        assertEquals(1, resultItem.getLookAheadSymbols().size());
        assertTrue(resultItem.getLookAheadSymbols().contains(EndOfChainSymbol.getInstance()));
    }
}
