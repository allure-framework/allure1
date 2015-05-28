/*global describe:true, it:true, beforeEach:true, afterEach:true, expect:true, spyOn:true, module:true, inject:true, angular:true, jasmine:true */
describe('GraphCtrl', function() {
    "use strict";
    var $controller, $rootScope;

    jasmine.qatools.fakePluginApi();
    beforeEach(module('allure.graph'));
    beforeEach(inject(function (_$controller_, _$rootScope_) {
        $controller = _$controller_;
        $rootScope = _$rootScope_;
    }));
    function createController(testcases) {
        var scope = $rootScope.$new();
        scope = scope.$new();
        $controller('GraphCtrl', {
            $scope: scope,
            data: {testCases: testcases},
            status: {
                all: ['FAILED', 'BROKEN', 'CANCELED', 'PASSED', 'PENDING']
            }
        });
        return scope;
    }

    it('should detect that all tests passed', function() {
        var scope = createController([{status: 'PASSED'}, {status: 'PASSED'}, {status: 'PASSED'}]);
        expect(scope.testsPassed).toBe(true);
    });

    it('should detect that some tests failed', function() {
        var scope = createController([{status: 'FAILED'}, {status: 'PASSED'}, {status: 'PASSED'}]);
        expect(scope.testsPassed).toBe(false);
    });

    it('should format pie-chart data', function() {
        var scope = createController([{status: 'FAILED'}, {status: 'PASSED'}, {status: 'PASSED'}]);
        expect(scope.statistic).toEqual({
            passed: 2, canceled: 0, failed: 1, broken: 0, pending: 0,
            total: 3
        });
        expect(scope.chartData).toEqual([
            {name: 'failed', value: 1, part: 1/3},
            {name: 'broken', value: 0, part: 0},
            {name: 'canceled', value: 0, part: 0},
            {name: 'passed', value: 2, part: 2/3},
            {name: 'pending', value: 0, part: 0}
        ]);
    });
});
