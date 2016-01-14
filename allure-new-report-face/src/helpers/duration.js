import {pad} from 'underscore.string';
import {isFunction} from 'underscore';

const dateTokens = [
    {
        suffix: 'd',
        method: time => Math.floor(time.valueOf() / (24 * 3600 * 1000))
    },
    {
        suffix: 'h',
        method: 'getUTCHours'
    },
    {
        suffix: 'm',
        method: 'getUTCMinutes'
    },
    {
        suffix: 's',
        method: 'getUTCSeconds'
    },
    {
        suffix: 'ms',
        method: 'getUTCMilliseconds'
    }
];

export default function(timeInt, count) {
    if(!timeInt) {
        return '0s';
    }
    const time = new Date(timeInt);
    const res = dateTokens.map(({method, suffix}) => ({
            value: isFunction(method) ? method(time) : time[method](),
            suffix
        }))
        .reduce(({hasValue, out}, token) => {
            hasValue = hasValue || token.value > 0;
            if(hasValue) {
                out.push(token);
            }
            return {hasValue, out};
        }, {hasValue: false, out: []})
        .out
        .map(function(token, index) {
            const value = index === 0 ? token.value : pad(token.value, 2);
            return value + token.suffix;
        });
    if(typeof count === 'number') {
        res.splice(0, count)
    }
    return res.join(' ');
}
