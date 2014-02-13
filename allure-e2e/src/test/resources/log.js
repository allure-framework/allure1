(function() {
    "use strict";
    function captureError(errorMessage) {
        window.allureErrors.push(errorMessage);
    }
    function override(obj, method, fn) {
        var oldFn = obj[method];

        obj[method] = oldFn ? function() {
            fn.apply(this, arguments);
            oldFn.apply(this, arguments);
        } : fn;
    }
    window.allureErrors = window.allureErrors || [];
    override(window, 'onerror', captureError);
    override(window.console, 'log', captureError);
    override(window.console, 'warn', captureError);
    override(window.console, 'error', captureError);
})();
