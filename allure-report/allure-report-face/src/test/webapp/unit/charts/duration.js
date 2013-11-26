/*global describe:true, it:true, beforeEach:true, afterEach:true, expect:true, spyOn:true, module:true, inject:true, angular:true, jasmine:true */
describe('SeverityMap', function () {
    'use strict';
    var scope, elem, filterSpy;

    beforeEach(module('allure.charts.duration', function($filterProvider) {
        $filterProvider.register('time', function() {
            return filterSpy = jasmine.createSpy('filterSpy').andCallFake(angular.identity);
        });
    }));
    jasmine.qatools.mockD3Tooltip();

    function createElement(html, scopeValues) {
        inject(function ($compile, $rootScope) {
            scope = $rootScope.$new();
            angular.extend(scope, scopeValues);
            elem = angular.element(html).appendTo('body');
            $compile(elem)(scope);
            scope.$digest();
        });
    }
    beforeEach(function() {
        createElement('<div duration data="data"></div>', {data: [
            {status: 'PASSED', time: {duration: 2}},
            {status: 'PASSED', time: {duration: 45}},
            {status: 'FAILED', time: {duration: 34}},
            {status: 'PASSED', time: {duration: 42}},
            {status: 'PASSED', time: {duration: 31}},
            {status: 'PASSED', time: {duration: 4}}
        ]});
        d3.timer.flush();
    });
    afterEach(function() {
        elem.remove();
    });

    it('should create graph and distribute testcases in intervals', function() {
        expect(elem.find('.bar').map(function() {
            return this.__data__.testcases.length;
        }).toArray()).toEqual([2, 0, 0, 0, 0, 0, 2, 0, 2]);
    });

    it('should format ticks using time filter', function() {
        expect(filterSpy).toHaveBeenCalled();
    });

    afterEach(function() {
        scope.$destroy();
    });
});