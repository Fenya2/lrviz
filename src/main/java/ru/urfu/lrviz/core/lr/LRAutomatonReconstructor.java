package ru.urfu.lrviz.core.lr;

import org.springframework.stereotype.Service;
import ru.urfu.lrviz.core.lr.lalr1.LALR1Item;
import ru.urfu.lrviz.core.lr.lr0.LR0Item;
import ru.urfu.lrviz.core.lr.lr1.LR1Item;
import ru.urfu.lrviz.core.lr.operations.*;

import java.util.*;

/**
 * Восстанавливает LR-автомат из его лога построения
 *
 * @author fenya
 * @since 30.03.2026
 */
@Service
public class LRAutomatonReconstructor {
    private static final String UNEXPECTED_OPERATION_MESSAGE_PREFIX = "Unexpected operation[%d] (%s)";

    public LRAutomaton reconstruct(BuildLog buildLog) {
        return reconstructUntil(buildLog, buildLog.getOperations().size());
    }

    public LRAutomaton reconstructUntil(BuildLog buildLog, int operationNumber) {
        List<BuildOperation> operations = buildLog.getOperations();
        if (operationNumber < 0 || operationNumber > operations.size()) {
            throw new IllegalArgumentException("Illegal final operation number.");
        }
        Map<String, LRState> namedStates = new LinkedHashMap<>();
        Map<LRAutomaton.TransitionKey, String> transitions = new LinkedHashMap<>();
        for (int i = 0; i < operationNumber; i++) {
            BuildOperation buildOperation = operations.get(i);
            if (OperationLevel.COMMENT.equals(buildOperation.level)) {
                continue;
            }
            processActionOperation(i, buildOperation, transitions, namedStates);
        }
        return new LRAutomaton(namedStates, transitions);
    }

    private static void processActionOperation(int operationNumber, BuildOperation buildOperation, Map<LRAutomaton.TransitionKey, String> transitions, Map<String, LRState> namedStates) {
        switch (buildOperation) {
            case AddStateOperation addState -> namedStates.put(addState.stateName, new LRState());
            case DeleteStateOperation deleteState -> namedStates.remove(deleteState.stateName);
            case AddTransitionOperation addTransition ->
                    transitions.put(new LRAutomaton.TransitionKey(addTransition.from, addTransition.through), addTransition.to);
            case DeleteTransitionOperation deleteTransition ->
                    transitions.remove(new LRAutomaton.TransitionKey(deleteTransition.from, deleteTransition.through));
            case AddItemInStateOperation addItem -> {
                checkStateExist(addItem.stateName, namedStates, operationNumber, buildOperation);
                LRState state = namedStates.get(addItem.stateName);
                Set<LRItem> stateItems = new LinkedHashSet<>(state.items());
                stateItems.add(addItem.item);
                namedStates.put(addItem.stateName, new LRState(stateItems));
            }
            case AddLookAheadOperation addLookAhead -> {
                checkStateExist(addLookAhead.stateName, namedStates, operationNumber, buildOperation);
                Set<LRItem> stateItems = namedStates.get(addLookAhead.stateName).items();
                LRItem targetItem = findTargetItem(addLookAhead, stateItems, operationNumber, buildOperation);
                if (targetItem instanceof LALR1Item lalr1Item) {
                    lalr1Item.addLookAhead(addLookAhead.lookAheadSymbol);
                    break;
                }
                HashSet<LRItem> lrItems = new LinkedHashSet<>(stateItems);
                LALR1Item newItem = null;
                if (targetItem instanceof LR0Item lr0Item) {
                    newItem = LALR1Item.fromLr0Item(lr0Item);
                }
                if (targetItem instanceof LR1Item lr1Item) {
                    newItem = LALR1Item.fromLr1Item(lr1Item);
                }
                if (newItem == null) {
                    throw new IllegalStateException(createUnexpectedOperationMessage(operationNumber, buildOperation, "Unexpected item type."));
                }
                newItem.addLookAhead(addLookAhead.lookAheadSymbol);
                lrItems.remove(targetItem);
                lrItems.add(newItem);
                namedStates.put(addLookAhead.stateName, new LRState(lrItems));
            }
            default ->
                    throw new IllegalStateException(createUnexpectedOperationMessage(operationNumber, buildOperation, "operation type not supported."));
        }
    }

    private static LRItem findTargetItem(AddLookAheadOperation addLookAhead, Set<LRItem> stateItems, int i, BuildOperation buildOperation) {
        return stateItems.stream()
                .filter(item -> LRItem.equalsByBasePart(item, addLookAhead.item))
                .findFirst().orElseThrow(() -> new IllegalStateException(createUnexpectedOperationMessage(i, buildOperation, "Item not added yet.")));
    }

    private static void checkStateExist(String stateName, Map<String, LRState> namedStates, int operationNumber, BuildOperation operation) {
        if (!namedStates.containsKey(stateName)) {
            throw new IllegalStateException(createUnexpectedOperationMessage(operationNumber, operation, "State not added yet."));
        }
    }

    private static String createUnexpectedOperationMessage(int operationNumber, BuildOperation operation, String detailedMessage) {
        return UNEXPECTED_OPERATION_MESSAGE_PREFIX.formatted(operationNumber, operation.message) + ": " + detailedMessage;
    }
}
