/*global modules*/
modules.require(['BH'], function(BH) {
    BH.match('input__control', function(ctx, json) {
        if(json.mods.required) {
            ctx.attr('required', true);
        }
    });
});

export default function(el, bemjson, cb) {
    modules.require(['i-bem__dom', 'BEMHTML'], (BEMDOM, BEMHTML) => {
        BEMDOM.update(el, BEMHTML.apply(bemjson));
        if(cb) {
            cb(el);
        }
    });
}

export function bemDestroy(el) {
    modules.require(['i-bem__dom'], (BEMDOM) => {
        BEMDOM.destruct(el);
    });
}
