/*global describe, it, beforeEach, afterEach, expect, spyOn, jasmine, angular, inject, module, d3 */
describe('Allure chart directives', function () {
    'use strict';
    beforeEach(module('allure.charts.util'));
    describe('Tooltip', function() {
        var $document,
            d3,
            tooltip,
            Tooltip;
        beforeEach(inject(function (_$document_, _d3_, d3Tooltip) {
            Tooltip = d3Tooltip;
            d3 = _d3_;
            $document = _$document_;
        }));

        it('should show tooltip with static text', function() {
            var element = d3.select(document.createElement('div'));
            tooltip = new Tooltip(element, 'static text');
            expect($document.find('.d3-tooltip').length).toBe(1);
            jasmine.qatools.triggerMouseEvent(element.node(), 'mouseover');
            expect($document.find('.d3-tooltip')[0].style.opacity).toBe('0.9');
            expect($document.find('.d3-tooltip').html()).toBe('static text');

            jasmine.qatools.triggerMouseEvent(element.node(), 'mouseout');
            expect($document.find('.d3-tooltip')[0].style.opacity).toBe('0');
        });

        it('should show tooltip with bindings', function() {
            var element = d3.select(document.createElement('div')).data([{title: 'test'}]);
            tooltip = new Tooltip(element, 'Tooltip {{title}}');
            jasmine.qatools.triggerMouseEvent(element.node(), 'mouseover');
            expect($document.find('.d3-tooltip').html()).toBe('Tooltip test');
        });

        it('should can override class name', function() {
            var element = d3.select(document.createElement('div'));
            tooltip = new Tooltip(element, 'tooltip', {tooltipCls: 'd3-tooltip test-cls'});
            expect(tooltip.tooltipCls).toBe('d3-tooltip test-cls');
            expect($document.find('.test-cls').length).toBe(1);
        });

        it('should can override other methods', function() {
            var showSpy = jasmine.createSpy('showSpy'),
                element = d3.select(document.createElement('div'));
            tooltip = new Tooltip(element, 'tooltip', {show: showSpy});
            jasmine.qatools.triggerMouseEvent(element.node(), 'mouseover');
            expect(showSpy).toHaveBeenCalled();
        });

        afterEach(function() {
            tooltip.destroy();
            expect($document.find('.d3-tooltip').length).toBe(0);
        });
    });

    describe('SvgViewport', function() {
        var $rootScope,
            d3Util,
            wrapper;
        beforeEach(inject(function (_$rootScope_, _d3Util_) {
            d3Util = _d3Util_;
            $rootScope = _$rootScope_;
        }));

        beforeEach(function() {
            wrapper = angular.element('<div></div>');
            wrapper.appendTo('body');
        });
        afterEach(function() {
            wrapper.remove();
        });

        function createViewport(wrapper, config) {
            var viewport = new d3Util.SvgViewport(wrapper[0], config);
            $rootScope.$apply();
            return angular.element(viewport.node());
        }

        function resizeWrapper(width, height) {
            wrapper.width(width);
            wrapper.height(height);
            $rootScope.$apply();
        }

        it('should create viewport with margins', function() {
            var viewport = createViewport(wrapper, {width: 100, height: 100}),
                container = viewport.find('.container-group');
            expect(container.attr('transform')).toBe('translate(20,20)');
        });

        it('should fit size in wrapper', function() {
            var viewport = createViewport(wrapper, {width: 160, height: 110});
            resizeWrapper(200, null);
            expect(viewport.width()).toBe(200);
            expect(viewport.height()).toBe(150);
        });

        it("should fit to height when viewport is vertical", function() {
            var viewport = createViewport(wrapper, {width: 110, height: 160});
            resizeWrapper(200, 200);
            expect(viewport.width()).toBe(150);
            expect(viewport.height()).toBe(200);
        });

        it('should handle element resizing', function() {
            var viewport = createViewport(wrapper, {width: 160, height: 110});
            resizeWrapper(200, null);
            resizeWrapper(400, null);
            expect(viewport.width()).toBe(400);
            expect(viewport.height()).toBe(300);
        });

        it('it should handle size reducing', function() {
            var viewport = createViewport(wrapper, {width: 160, height: 110});
            resizeWrapper(200, null);
            resizeWrapper(100, null);
            expect(viewport.width()).toBe(100);
            expect(viewport.height()).toBe(75);
        });
    });
});