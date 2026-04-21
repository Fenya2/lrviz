const grammar = {
    terminals: [],
    nonTerminals: [],
    rules: [],
    startSymbol: ''
};

automatonType = 'lr0'

const buildOptions = {
    namesGenerationStrategy: 'byTransitionSymbol',
    lalr1BuildAlgorithm: 'classic',
    enableBuildLog: true
}

const visualizeOptions = {
    colorizeTransitions: false,
    colorizeStateNames: false
}

presentation = 'application/json'
