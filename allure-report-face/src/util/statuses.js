const statuses = {
    failed: '#fd5a3e',
    broken: '#ffd963',
    canceled: '#ccc',
    pending: '#d35ebe',
    passed: '#97cc64'
};

const colors = function() {
    let colors = [];
    for (let status in statuses) {
        colors.push(statuses[status]);
    }
    return colors;
}();

const states = function() {
    let states = [];
    for (let status in statuses) {
        states.push(status);
    }
    return states;
}();

export default {
    states, colors
};