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

presentation = 'application/json'