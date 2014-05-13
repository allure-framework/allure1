/*global describe:true, it:true, beforeEach:true, afterEach:true, expect:true, spyOn:true, module:true, inject:true, angular:true, jasmine:true */
describe('DurationChart', function () {
    'use strict';
    var scope, elem, filterSpy;

    beforeEach(module('allure.charts.duration', function($filterProvider) {
        $filterProvider.register('d3time', function() {
            return filterSpy = jasmine.createSpy('filterSpy').andCallFake(angular.identity);
        });
    }));
    jasmine.qatools.mockD3Tooltip();

    var defaultData = {data: [
        {status: 'PASSED', time: {duration: 2}},
        {status: 'PASSED', time: {duration: 45}},
        {status: 'FAILED', time: {duration: 34}},
        {status: 'PASSED', time: {duration: 42}},
        {status: 'PASSED', time: {duration: 31}},
        {status: 'PASSED', time: {duration: 4}}
    ]};

    function extractData(index, elem) {
        return elem.__data__.testcases.length;
    }

    function createElement(html, scopeValues) {
        inject(function ($compile, $rootScope) {
            scope = $rootScope.$new();
            angular.extend(scope, scopeValues || defaultData);
            elem = angular.element(html).appendTo('body');
            $compile(elem)(scope);
            scope.$digest();
        });
    }
    beforeEach(function() {
        d3.timer.flush();
    });
    afterEach(function() {
        elem.remove();
    });

    it('should create graph and distribute testcases in intervals', function() {
        createElement('<div duration data="data"></div>');
        expect(elem.find('.bar').map(extractData).toArray()).toEqual([2, 0, 0, 0, 0, 0, 2, 0, 2]);
    });

    it('should format ticks using time filter', function() {
        createElement('<div duration data="data"></div>');
        expect(filterSpy).toHaveBeenCalled();
    });

    it('should create diagram only with zero durations', function() {
        createElement('<div duration data="data"></div>', { data: [
            {status: 'CANCELED', time: {duration:0}},
            {status: 'CANCELED', time: {duration:0}}
        ]});
        expect(elem.find('.bar').map(extractData).toArray()).toEqual([2]);
    });

    afterEach(function() {
        scope.$destroy();
    });
});