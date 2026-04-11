const TERMINAL = 'terminal'
const NONTERMINAL = 'nonTerminal'

// ========== Элементы DOM ==========
const terminalInput = document.getElementById('terminalInputField');
const terminalInputApplyButton = document.getElementById('addTerminalButton');
const terminalsList = document.getElementById('terminalsList');

const nonTerminalInput = document.getElementById('nonTerminalInputField');
const nonTerminalInputApplyButton = document.getElementById('addNonTerminalButton');
const nonTerminalsList = document.getElementById('nonTerminalsList');

const leftPartRuleSelector = document.getElementById('leftPartRuleSelector');
const rightPartRuleInputField = document.getElementById('rightPartRuleInputField');
const addRuleButton = document.getElementById('addRuleButton');
const rulesList = document.getElementById('rulesList');

const startSymbolSelector = document.getElementById('startSymbolSelector');

// Перерисовка списка правил на основе grammar.rules
function renderRules() {
    rulesList.innerHTML = '';
    grammar.rules.forEach((rule, index) => {
        const ruleDiv = document.createElement('div');
        ruleDiv.textContent = `${rule.left} → ${rule.right}`;
        const removeBtn = document.createElement('button');
        removeBtn.textContent = '✖';
        removeBtn.addEventListener('click', (e) => {
            e.stopPropagation();
            removeRuleByIndex(index);
        });
        ruleDiv.appendChild(removeBtn);
        rulesList.appendChild(ruleDiv);
    });
}

// Удаление правила по индексу
function removeRuleByIndex(index) {
    grammar.rules.splice(index, 1);
    renderRules();
}

// Удалить все правила, в которых встречается символ (в левой или правой части)
function removeRulesContainingSymbol(symbol) {
    const initialLength = grammar.rules.length;
    grammar.rules = grammar.rules.filter(rule => rule.left !== symbol && !rule.right.includes(symbol));
    if (grammar.rules.length !== initialLength) {
        renderRules();
    }
}

// Обновить селекторы, которые зависят от нетерминалов (левая часть правил и стартовый символ)
function updateNonTerminalSelectors() {
    // Сохраняем выбранные значения
    const selectedLeft = leftPartRuleSelector.value;
    const selectedStart = startSymbolSelector.value;

    // Перестраиваем leftPartRuleSelector
    leftPartRuleSelector.innerHTML = '<option value="" selected disabled>-- выберите нетерминал --</option>';
    grammar.nonTerminals.forEach(nt => {
        const option = document.createElement('option');
        option.value = nt;
        option.textContent = nt;
        leftPartRuleSelector.appendChild(option);
    });

    // Перестраиваем startSymbolSelector
    startSymbolSelector.innerHTML = '<option value="" selected disabled>-- выберите --</option>';
    grammar.nonTerminals.forEach(nt => {
        const option = document.createElement('option');
        option.value = nt;
        option.textContent = nt;
        startSymbolSelector.appendChild(option);
    });

    // Восстанавливаем выбранные значения, если они ещё существуют
    if (selectedLeft && grammar.nonTerminals.includes(selectedLeft)) {
        leftPartRuleSelector.value = selectedLeft;
    } else {
        leftPartRuleSelector.value = '';
    }

    if (selectedStart && grammar.nonTerminals.includes(selectedStart)) {
        startSymbolSelector.value = selectedStart;
    } else {
        startSymbolSelector.value = '';
        if (grammar.startSymbol && !grammar.nonTerminals.includes(grammar.startSymbol)) {
            grammar.startSymbol = '';
        }
    }
}

// Добавление элемента (терминала или нетерминала) с проверкой дубликатов
function addSymbol(type, value) {
    if (!value || value.trim() === '') return false;

    if(grammar.terminals.includes(value) || grammar.nonTerminals.includes(value)) {
        alert(`Символ "${value}" уже существует!`);
        return false;
    }

    if (type === TERMINAL) {
        grammar.terminals.push(value);
        const span = createSymbolSpan(value, () => removeTerminal(value));
        terminalsList.appendChild(span);
        return true;
    }
    else if (type === NONTERMINAL) {
        grammar.nonTerminals.push(value);
        const span = createSymbolSpan(value, () => removeNonTerminal(value));
        nonTerminalsList.appendChild(span);
        updateNonTerminalSelectors();
        return true;
    }
    return false;
}

function createSymbolSpan(symbol, onRemove) {
    const span = document.createElement('span');
    span.appendChild(document.createTextNode(symbol));
    const removeBtn = document.createElement('button');
    removeBtn.textContent = '-';
    removeBtn.addEventListener('click', () => onRemove());
    span.appendChild(removeBtn);
    return span;
}

// Удаление терминала
function removeTerminal(value) {
    const index = grammar.terminals.indexOf(value);
    if (index !== -1) {
        grammar.terminals.splice(index, 1);
        // Удаляем все правила, содержащие этот терминал
        removeRulesContainingSymbol(value);
        // Удаляем визуальный элемент
        const items = terminalsList.querySelectorAll('span');
        for (let item of items) {
            if (item.firstChild?.textContent === value) {
                item.remove();
                break;
            }
        }
    }
}

// Удаление нетерминала
function removeNonTerminal(value) {
    const index = grammar.nonTerminals.indexOf(value);
    if (index !== -1) {
        grammar.nonTerminals.splice(index, 1);
        // Удаляем правила, где участвует этот нетерминал
        removeRulesContainingSymbol(value);
        // Удаляем визуальный элемент
        const items = nonTerminalsList.querySelectorAll('span');
        for (let item of items) {
            if (item.firstChild?.textContent === value) {
                item.remove();
                break;
            }
        }
        // Если удалённый нетерминал был стартовым символом, сбрасываем
        if (grammar.startSymbol === value) {
            grammar.startSymbol = '';
        }
        // Обновляем селекторы
        updateNonTerminalSelectors();
    }
}

// Валидация правой части: все символы должны быть либо терминалами, либо нетерминалами
function isValidRightPart(rightStr) {
    if (rightStr === '') return false;
    const symbols = rightStr.split('');
    for (let sym of symbols) {
        if (!grammar.terminals.includes(sym) && !grammar.nonTerminals.includes(sym)) {
            return false;
        }
    }
    return true;
}

// Добавление правила
function addRule() {
    const left = leftPartRuleSelector.value;
    const right = rightPartRuleInputField.value.trim();

    if (!left) {
        alert('Выберите левую часть (нетерминал)');
        return;
    }
    if (!right) {
        alert('Правая часть не может быть пустой');
        return;
    }
    if (!isValidRightPart(right)) {
        alert('Правая часть может содержать только терминалы и нетерминалы грамматики');
        return;
    }

    const isDuplicate = grammar.rules.some(rule => rule.left === left && rule.right === right);
    if (isDuplicate) {
        alert(`Правило ${left} → ${right} уже существует`);
        return;
    }

    grammar.rules.push({ left, right });
    renderRules();
    rightPartRuleInputField.value = '';
}

// Выбор стартового символа (аксиомы)
function selectStartSymbol(value) {
    if (!value) {
        grammar.startSymbol = '';
        return;
    }
    if (!grammar.nonTerminals.includes(value)) {
        alert('Стартовый символ должен быть нетерминалом');
        startSymbolSelector.value = grammar.startSymbol || '';
        return;
    }
    grammar.startSymbol = value;
}

// ========== Обработчики событий ==========
terminalInput.addEventListener('change', () => {
    addSymbol(TERMINAL, terminalInput.value);
    terminalInput.value = '';
});
nonTerminalInput.addEventListener('change', () => {
    addSymbol(NONTERMINAL, nonTerminalInput.value);
    nonTerminalInput.value = '';
});
terminalInputApplyButton.addEventListener('click', () => {
    addSymbol(TERMINAL, terminalInput.value);
    terminalInput.value = '';
});
nonTerminalInputApplyButton.addEventListener('click', () => {
    addSymbol(NONTERMINAL, nonTerminalInput.value);
    nonTerminalInput.value = '';
});

addRuleButton.addEventListener('click', addRule);
rightPartRuleInputField.addEventListener('keypress', function(event) {
    if (event.key === 'Enter') {
        addRule()
    }
});

startSymbolSelector.addEventListener('change', (e) => {
    selectStartSymbol(e.target.value);
});