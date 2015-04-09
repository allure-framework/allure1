/*global describe:true, it:true, beforeEach:true, afterEach:true, expect:true, spyOn:true, module:true, inject:true, angular:true, jasmine:true */
describe("timeline", function() {
    "use strict";
    var $controller, $rootScope;

    jasmine.qatools.fakePluginApi();
    beforeEach(module('ui.router'));
    beforeEach(module('allure.timeline'));
    beforeEach(inject(function (_$controller_, _$rootScope_) {
        $controller = _$controller_;
        $rootScope = _$rootScope_;
    }));

    describe('TimelineCtrl', function() {
        function createController() {
            var scope = $rootScope.$new();
            scope = scope.$new();
            $controller('TimelineCtrl', {
                $scope: scope,
                $state: {},
                data: {"hosts": [{
                    threads: [{
                        testCases: [
                            {time: {start: 10, stop: 12}},
                            {time: {start: 15, stop: 16}}
                        ]
                    }, {
                        testCases: [
                            {time: {start: 42, stop: 50}}
                        ]
                    }]
                }, {
                    threads: [{
                        testCases: [
                            {time: {start: 11, stop: 48}}
                        ]
                    }]
                }]}
            });
            return scope;
        }

        it("should find minimal start time", function () {
            var scope = createController();
            expect(scope.startTime).toBe(10);
            expect(scope.timeRange).toEqual([0,40]);
        });
    });
})
