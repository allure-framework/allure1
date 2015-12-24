/*global modules*/
modules.require(['BH'], function(BH) {
    BH.beforeEach(function(ctx, json) {
        if(json.block && !json.elem) {
            if(!json.mods.size) {
                ctx.mod('size', 'm');
            }
            ctx.mod('theme', 'islands');
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
