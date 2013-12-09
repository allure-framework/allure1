/**
 * jasmine helpers pack
 */
/*global angular, beforeEach, afterEach, jasmine*/
(function(jasmine) {
    'use strict';
    if(!jasmine) {
        return;
    }
    jasmine.qatools = {
        triggerMouseEvent: function(el, type) {
            var ev = document.createEvent("MouseEvent");
            ev.initMouseEvent(
                type,
                true /* bubble */, true /* cancelable */,
                window, null,
                0, 0, 0, 0, /* coordinates */
                false, false, false, false, /* modifier keys */
                0 /*left*/, null
            );
            el.dispatchEvent(ev);
        },
        mockD3Tooltip: function() {
            var tooltipsCount = 0;
            beforeEach(module(function($provide) {
                $provide.value('d3Tooltip', function() {
                    tooltipsCount++;
                    return angular.extend(jasmine.createSpyObj('tooltip', ['show', 'hide']), {destroy: function() {
                        tooltipsCount--;
                    }});
                });
            }));
            afterEach(function() {
                expect(tooltipsCount).toBe(0);
            });
        }
    };
    beforeEach(function() {
        this.addMatchers({
            toHaveClass: function(cls) {
                this.message = function() {
                    return "Expected '" + angular.mock.dump(this.actual) + "' to have class '" + cls + "'.";
                };
                return this.actual.hasClass(cls);
            },
            toEqualData: function(expected) {
                return angular.equals(this.actual, expected);
            }
        });
    });
})(jasmine);

