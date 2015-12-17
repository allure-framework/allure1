export * from 'backbone-decorators';

export function protoprop(target, name, descriptor) {
    target[name] = descriptor.initializer();
}

export function className(name) {
    return function(target) {
        target.prototype.className = name;
    };
}

export function region(selector) {
    return function(target, name, descriptor) {
        delete descriptor.initializer;
        descriptor.writable = true;
        target.regions = Object.assign({
            [name]: selector
        }, target.regions);
    };
}
