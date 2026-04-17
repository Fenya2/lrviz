const buildContainer = document.getElementById('buildContainer')

buildLog = null
cy = null

// Step-by-step visualization state
let currentStep = 0
let totalSteps = 0
let automatonData = null
let buildOperations = []
let navigationControls = null
let stepCounterDisplay = null

document.getElementById('buildButton').addEventListener('click', () => {
    if(grammar.terminals.length === 0
        || grammar.nonTerminals.length === 0
        || grammar.rules.length === 0
        || grammar.startSymbol === '') {
            alert('Грамматика не задана.');
            return;
    }

fetch('/api/v1/build/' + automatonType, {
        method: 'POST',
        headers: {
            'Accept': presentation,
            'Content-Type': 'application/json',
        },

        body: JSON.stringify({
            grammar: grammar,
            buildOptions: buildOptions
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Ошибка при обращении к API');
        }
        const contentType = response.headers.get('Content-Type');
        if (contentType && contentType.includes('image/png')) {
            return response.blob().then(blob => ({ type: 'image', data: blob }));
        } else if (contentType && contentType.includes('application/octet-stream')) {
            return response.blob().then(blob => ({ type: 'zip', data: blob }));
        } else {
            return response.json().then(data => ({ type: 'json', data: data }));
        }
    })
    .then(result => {
        if (window.cy && typeof window.cy.destroy === 'function') {
            window.cy.destroy();
            window.cy = null;
        }
        buildContainer.innerHTML = '';
        if (result.type === 'image') {
            const imageUrl = URL.createObjectURL(result.data);
            const img = document.createElement('img');
            img.src = imageUrl;
            img.alt = 'Automaton visualization';
            buildContainer.appendChild(img);
            img.onload = () => URL.revokeObjectURL(imageUrl);
            return
        }

        if (result.type === 'zip') {
            const url = URL.createObjectURL(result.data);
            const a = document.createElement('a');
            a.href = url;
            a.download = 'buildLog.zip';
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            URL.revokeObjectURL(url);
            buildContainer.innerHTML = '<p style="color: green; font-size: 18px;">ZIP-архив с картинками скачан: buildLog.zip</p>';
            return
        }

        buildOperations = result.data.buildLog.operations
        automatonData = result.data.automaton
        totalSteps = buildOperations.length
        currentStep = 0

        operations = result.data.buildLog.operations
        buildLogContainer = document.createElement('pre')
        for (let i = 0; i < operations.length; i++) {
            const op = operations[i];
            const logLine = document.createElement('code');
            logLine.textContent = `[${i}] ${op.level.toUpperCase()} ${op.name}: ${op.message}`;
            buildLogContainer.appendChild(logLine);
            buildLogContainer.appendChild(document.createElement('br'));
        }
        buildContainer.appendChild(buildLogContainer)

        createNavigationControls()
        graphContainer = document.createElement('div')
        graphContainer.id = 'graphContainer'
        graphContainer.style.width = '100%'
        graphContainer.style.height = window.innerHeight.toString() + 'px'
        buildContainer.appendChild(graphContainer)

        // Render initial step
        renderCurrentStep()
    })
    .catch(error => {
        alert('Ошибка.');
    });
});

function createNavigationControls() {
    navigationControls = document.createElement('div')
    navigationControls.id = 'navigationControls'

    const firstButton = document.createElement('button')
    firstButton.id = 'firstStepButton'
    firstButton.className = 'switchOperationButton'
    firstButton.textContent = '|<'
    firstButton.onclick = () => goToStep(0)

    const prevButton = document.createElement('button')
    prevButton.id = 'prevStepButton'
    prevButton.className = 'switchOperationButton'
    prevButton.textContent = '<'
    prevButton.onclick = () => goToStep(currentStep - 1)

    stepCounterDisplay = document.createElement('span')
    stepCounterDisplay.id = 'stepCounter'
    stepCounterDisplay.textContent = 'Операция 0 из 0'

    const nextButton = document.createElement('button')
    nextButton.id = 'nextStepButton'
    nextButton.className = 'switchOperationButton'
    nextButton.textContent = '>'
    nextButton.onclick = () => goToStep(currentStep + 1)

    const lastButton = document.createElement('button')
    lastButton.id = 'lastStepButton'
    lastButton.className = 'switchOperationButton'
    lastButton.textContent = '>|'
    lastButton.onclick = () => goToStep(totalSteps)

    const stepInput = document.createElement('input')
    stepInput.id = 'stepInput'
    stepInput.type = 'number'
    stepInput.min = '0'
    stepInput.max = totalSteps.toString()
    stepInput.placeholder = '0'
    stepInput.style.marginLeft = '10px'
    stepInput.style.width = '120px'

    const goButton = document.createElement('button')
    goButton.id = 'goStepButton'
    goButton.textContent = '✔'
    goButton.onclick = () => {
        const stepNum = parseInt(stepInput.value)
        if (!isNaN(stepNum) && stepNum >= 0 && stepNum <= totalSteps) {
            goToStep(stepNum)
        } else {
            alert("Недопустимый номер операции.")
        }
    }

    stepInput.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') {
            goButton.click()
        }
    })

    navigationControls.appendChild(firstButton)
    navigationControls.appendChild(prevButton)
    navigationControls.appendChild(stepCounterDisplay)
    navigationControls.appendChild(nextButton)
    navigationControls.appendChild(lastButton)
    navigationControls.appendChild(stepInput)
    navigationControls.appendChild(goButton)

    buildContainer.appendChild(navigationControls)
}

function goToStep(step) {
    currentStep = Math.max(0, Math.min(totalSteps, step))
    renderCurrentStep()
}

function renderCurrentStep() {
    stepCounterDisplay.textContent = `${currentStep}/${totalSteps}`
    const automatonState = reconstructAutomatonAtStep(currentStep)
    renderAutomaton(automatonState)
    const buttons = navigationControls.querySelectorAll('button')
    buttons[0].disabled = currentStep === 0  // First
    buttons[1].disabled = currentStep === 0  // Previous
    buttons[2].disabled = currentStep === totalSteps  // Next
    buttons[3].disabled = currentStep === totalSteps  // Last

    const stepInput = document.getElementById('stepInput')
    if (stepInput) {
        stepInput.max = totalSteps.toString()
    }
}

function reconstructAutomatonAtStep(stepIndex) {
    const states = new Map()
    const transitions = []

    for (let i = 0; i <= stepIndex && i < buildOperations.length; i++) {
        const op = buildOperations[i]
        switch (op.name) {
            case 'addState':
                states.set(op.stateName, {
                    name: op.stateName,
                    items: []
                })
                break

            case 'deleteState':
                states.delete(op.stateName)
                break

            case 'addTransition':
                transitions.push({
                    from: op.from,
                    to: op.to,
                    through: op.through
                })
                break

            case 'deleteTransition':
                const transIndex = transitions.findIndex(t =>
                    t.from === op.from && t.to === op.to && t.through === op.through
                )
                if (transIndex !== -1) {
                    transitions.splice(transIndex, 1)
                }
                break

            case 'addItem':
                if (states.has(op.state)) {
                    const state = states.get(op.state)
                    state.items.push(op.item)
                }
                break

            case 'addLookAhead':
                if (states.has(op.state)) {
                    const state = states.get(op.state)
                    const item = state.items.find(item =>
                        item.rule === op.item.rule && item.dotIndex === op.item.dotIndex
                    )
                    if (item) {
                        if (!item.lookAheadSymbols) {
                            item.lookAheadSymbols = []
                        }
                        if (op.lookAheadSymbol && !item.lookAheadSymbols.includes(op.lookAheadSymbol)) {
                            item.lookAheadSymbols.push(op.lookAheadSymbol)
                        }
                    }
                }
                break
        }
    }

    const result = {
        states: Array.from(states.values()),
        transitions: transitions
    }
    return result
}

function renderAutomaton(automatonState) {
    const graphContainer = document.getElementById('graphContainer')
    graphContainer.style.visibility = 'hidden'

    if (window.cy && typeof window.cy.destroy === 'function') {
        window.cy.destroy()
    }

    const elements = []
    automatonState.states.forEach(state => {
        elements.push({
            group: 'nodes',
            data: {
                id: state.name
            }
        })
    })
    automatonState.transitions.forEach(trans => {
        elements.push({
            group: 'edges',
            data: {
                id: `${trans.from}-${trans.through}-${trans.to}`,
                source: trans.from,
                target: trans.to,
                label: trans.through
            }
        })
    })
    window.cy = cytoscape({
        container: graphContainer,
        elements: elements,
        style: [
            {
                selector: 'node',
                style: {
                    'shape': 'rectangle',
                    'background-opacity': 0,
                    'border-opacity': 0
                }
            },
            {
                selector: 'edge',
                style: {
                    'label': 'data(label)',
                    'width': 2,
                    'line-color': '#000',
                    'target-arrow-color': '#000',
                    'target-arrow-shape': 'triangle',
                    'curve-style': 'unbundled-bezier',
                    'font-size': 20,
                    'loop-direction': '0deg',
                    'loop-sweep': '20deg',
                    'text-margin-y': -15,
                }
            }
        ],
        layout: {
            name: 'cose',
            directed: true,
        }
    })

    window.cy.nodeHtmlLabel([
        {
            query: 'node',
            tpl: function(data) {
                const state = automatonState.states.find(s => s.name === data.id);
                if (!state) return '';
                let html = `<table id="table-${data.id}" style="border: 0; background-color: white; font-family: monospace; font-size: 14px;">`;
                html += '<tr>';
                html += '<td bgcolor="black" align="center">';
                html += `<font color="white">${data.id}</font>`;
                html += '</td>';
                html += '</tr>';
                if (state.items && state.items.length > 0) {
                    state.items.forEach(item => {
                        const left = item.rule.left;
                        const right = item.rule.right;
                        const beforeDot = right.substring(0, item.dotIndex);
                        const afterDot = right.substring(item.dotIndex);

                        html += '<tr>';
                        html += '<td align="left">';
                        html += `<font color="black">`;
                        html += `[${left} → ${beforeDot}•${afterDot}]`;
                        if (item.lookAheadSymbols && item.lookAheadSymbols.length > 0) {
                            html += `, ${item.lookAheadSymbols.join('/')}`;
                        }
                        html += '</font>';
                        html += '</td>';
                        html += '</tr>';
                    });
                }
                html += '</table>';
                return html;
            }
        }
    ])

    function waitForLabelsAndAdjust() {
        const allTablesRendered = window.cy.nodes().every(node => {
            const tableId = `table-${node.id()}`
            return document.getElementById(tableId) !== null
        })

        if (allTablesRendered) {
            window.cy.nodes().forEach(node => {
                const tableId = `table-${node.id()}`
                const table = document.getElementById(tableId)
                node.style('width', table.offsetWidth)
                node.style('height', table.offsetHeight)
            })

            window.cy.layout({
                name: 'cose',
                directed: true
            }).run()

            window.cy.one('layoutstop', () => {
                graphContainer.style.visibility = 'visible'
            })
        } else {
            requestAnimationFrame(waitForLabelsAndAdjust)
        }
    }

    waitForLabelsAndAdjust()
}

function formatRuleText(rule) {
    return `${rule.left} -> ${rule.right}`
}
