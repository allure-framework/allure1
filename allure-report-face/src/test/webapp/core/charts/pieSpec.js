/*global describe, it, beforeEach, afterEach, expect, spyOn, jasmine, angular, inject, module, d3 */
describe('PieChart', function() {
    'use strict';
    var scope,
        elem;

    function createElement(html, scopeValues) {
        inject(function ($compile, $rootScope) {
            scope = $rootScope.$new();
            angular.extend(scope, scopeValues);
            elem = angular.element(html);
            $compile(elem)(scope);
            scope.$digest();
        });
    }

    beforeEach(module('allure.core.charts.pie'));
    jasmine.qatools.mockD3Tooltip();

    function expectStatusSelected(name) {
        expect(scope.currentStatus).toBe(name.toUpperCase());
        var selectedItems = d3.select(elem[0]).selectAll('.arc path').filter(function(d) {return d.selected;});
        expect(selectedItems.length).toBe(1);
        expect(selectedItems.data()[0].data.name).toBe(name);
    }

    it('should create piechart', function() {
        createElement('<div class="pie-chart" pie-chart selected="currentStatus" data="chartData"></div>', {chartData: [
            {name: 'test', value: 3},
            {name: 'test2', value: 1}
        ]});
        expect(elem.find('.arc').length).toBe(2);
        expect(elem.find('.legend>g').length).toBe(2);
    });

    it('should bind selected sector to scope', function() {
        createElement('<div class="pie-chart" pie-chart selected="currentStatus" data="chartData"></div>', {chartData: [
            {name: 'test', value: 3, part: 0.75},
            {name: 'test2', value: 1, part: 0.25}
        ]});
        jasmine.qatools.triggerMouseEvent(elem.find('.arc path')[0], 'click');
        expectStatusSelected('test');

        jasmine.qatools.triggerMouseEvent(elem.find('.arc path')[1], 'click');
        expectStatusSelected('test2');
    });

    it('should select sector from legend click', function() {
        createElement('<div class="pie-chart" pie-chart selected="currentStatus" data="chartData"></div>', {chartData: [
            {name: 'test', value: 3, part: 0.75},
            {name: 'test2', value: 1, part: 0.25}
        ]});
        jasmine.qatools.triggerMouseEvent(elem.find('.legend>g')[0], 'click');
        expectStatusSelected('test');
    });

    it('should pass default tooltip template', function() {
        createElement('<div class="pie-chart" pie-chart selected="currentStatus" data="chartData"></div>', {chartData: [
            {name: 'test', value: 3, part: 0.75},
            {name: 'test2', value: 1, part: 0.25}
        ]});
        inject(function(d3Tooltip) {
            expect(d3Tooltip.calls.mostRecent().args[1]).toBeDefined();
        });
    });

    it('should can customize tooltip', function() {
        createElement('<div class="pie-chart" pie-chart selected="currentStatus" data="chartData" tooltip-tpl="tooltipTpl"></div>', {
            chartData: [{name: 'test', value: 3, part: 0.75}, {name: 'test2', value: 1, part: 0.25}],
            tooltipTpl: 'test tooltip'
        });
        inject(function(d3Tooltip) {
            expect(d3Tooltip.calls.mostRecent().args[1]).toBe('test tooltip');
        });
    });

    afterEach(function() {
        scope.$destroy();
    });
});
