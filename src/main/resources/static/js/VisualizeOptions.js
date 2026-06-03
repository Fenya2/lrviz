document.getElementById('presentationSelector').addEventListener('change', (e) => {
    presentation = e.target.value
});

document.getElementById('colorizeTransitionsCheckbox').addEventListener('change', (e) => {
    visualizeOptions.colorizeTransitions = e.target.checked
});

document.getElementById('colorizeStateNamesCheckbox').addEventListener('change', (e) => {
    visualizeOptions.colorizeStateNames = e.target.checked
});

document.getElementById('stateNameStyleSelector').addEventListener('change', (e) => {
    visualizeOptions.stateNameStyle = e.target.value
});