document.getElementById('automatonSelector').addEventListener('change', (e) => {
    automatonType = e.target.value
    document.getElementById('lalr1BuildAlgorithmSelector').disabled = automatonType !== 'lalr1';
});