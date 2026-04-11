document.getElementById('namesGenerationStrategySelector').addEventListener('change', (e) => {
    buildOptions.namesGenerationStrategy = e.target.value
});

document.getElementById('lalr1BuildAlgorithmSelector').addEventListener('change', (e) => {
    buildOptions.lalr1BuildAlgorithm = e.target.value
});