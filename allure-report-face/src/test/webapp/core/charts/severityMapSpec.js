/*global describe:true, it:true, beforeEach:true, afterEach:true, expect:true, spyOn:true, module:true, inject:true, angular:true, jasmine:true */
describe('SeverityMap', function () {
    'use strict';
    var scope, elem;

    beforeEach(module('allure.core.charts.severityMap', function($provide) {
        $provide.value('status', {all: ['PASSED', 'FAILED', 'CANCELED']});
        $provide.value('severity', {all: ['CRITICAL', 'NORMAL', 'TRIVIAL']});
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
        createElement('<div severity-map data="data"></div>', {data: [
            {status: 'PASSED', severity: 'NORMAL'},
            {status: 'PASSED', severity: 'NORMAL'},
            {status: 'FAILED', severity: 'NORMAL'},
            {status: 'PASSED', severity: 'CRITICAL'},
            {status: 'PASSED', severity: 'CRITICAL'},
            {status: 'PASSED', severity: 'CRITICAL'}
        ]});
        d3.timer.flush();
    });
    afterEach(function() {
        elem.remove();
    });

    it('should group data and create graph', function() {
        expect(elem.find('.fill-passed:not([height="0"])').length).toBe(2);
        expect(elem.find('.fill-failed:not([height="0"])').length).toBe(1);
        expect(elem.find('.fill-canceled:not([height="0"])').length).toBe(0);
    });

    afterEach(function() {
        scope.$destroy();
    });
});
