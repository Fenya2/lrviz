document.getElementById('presentationSelector').addEventListener('change', (e) => {
    presentation = e.target.value
});

document.getElementById('colorizeOptionsCheckbox').addEventListener('change', (e) => {
    visualizeOptions.colorizeTransitions = e.target.checked
});