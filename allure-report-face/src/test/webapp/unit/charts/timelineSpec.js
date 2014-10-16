/*global jasmine, describe, it, beforeEach, afterEach, expect, spyOn, angular, inject, module, d3 */
describe('Timeline', function() {
    'use strict';
    var $timeout,
        scope,
        elem;

    function createElement(html, scopeValues) {
        inject(function ($compile, $rootScope, _$timeout_) {
            $timeout = _$timeout_;
            scope = $rootScope.$new();
            angular.extend(scope, scopeValues);
            elem = angular.element(html);
            $compile(elem)(scope);
            scope.$digest();
        });
    }

    function createTimeline() {
        createElement('<div timeline="data" range="interval" on-item-click="onItemClick(item)"></div>',
            {
                data: [{
                    title : "Default",
                    threads : [ {
                        title : "Thread 1",
                        testCases : [
                            { time : {start: 0, duration: 15, stop: 15}, title: 'test', uid: 'uid', status : "BROKEN" },
                            { time: {start: 5, duration: 17, stop: 22}, title: 'test2', uid: 'uid2', status: 'BROKEN' }
                        ]
                    }]
                }, {
                    title: "Second",
                    threads: [{
                        title: "000",
                        testCases: [
                            {time: {start: 15, duration: 11, stop: 26}, title: 'test3', uid: 'uid3', status: 'PASSED'}
                        ]
                    }]
                }],
                interval: [0, 30],
                onItemClick: jasmine.createSpy('onItemClick')
            }
        );
    }

    beforeEach(module('allure.charts.timeline', function($provide, $filterProvider) {
        $filterProvider.register('time', function() {
            return angular.identity;
        });
    }));
    jasmine.qatools.mockD3Tooltip();

    it('should create and update timeline', function() {
        createTimeline();
        scope.$apply();
        expect(elem.find('.bar').length).toBe(3);
        scope.data[1].threads.push({
            title: '001',
            testCases: [{time: {start: 23, duration: 7, stop: 30}, title: 'test4', uid: 'uid4', status: 'FAILED'}]
        });
        scope.data = scope.data.slice(0);
        scope.$apply();
        expect(elem.find('.bar').length).toBe(4);
    });

    it('should filter tests with zero duration', function() {
        createTimeline();
        scope.data[1].threads.push({
            title: '001',
            testCases: [{time: {start: 33, duration: 0, stop: 33}, title: 'test4', uid: 'uid4', status: 'FAILED'}]
        });
        scope.$apply();
        expect(elem.find('.bar').length).toBe(3);
    });

    it('should clear timeline when there is no tests', function() {
        createTimeline();
        scope.data = [];
        scope.$apply();
        expect(elem.find('.bar').length).toBe(0);
    });

    it('should group timestamps by host', function() {
        createTimeline();
        expect(elem.find('.timeline-group').length).toBe(2);
    });

    it('should open testcase on click', function() {
        createTimeline();
        jasmine.qatools.triggerMouseEvent(elem.find('.bar')[0], 'click');
        expect(scope.onItemClick).toHaveBeenCalled();
    });

    afterEach(function() {
        scope.$destroy();
    });
});
