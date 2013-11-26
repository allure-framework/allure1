/*global describe, it, beforeEach, afterEach, expect, spyOn, jasmine, angular, inject, module, d3 */
describe('Allure chart directives', function () {
    'use strict';
    describe('Tooltip', function() {
        var $document,
            d3,
            tooltip,
            Tooltip;
        beforeEach(module('allure.charts.util'));
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
});