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
        createElement('<div timeline range="range" on-item-click="onItemClick(item)"><timestamp data="time" ng-repeat="time in times"></div>',
            {
                times: [
                    {time: {start: 0, duration: 15, stop: 15}, title: 'test', uid: 'uid', status: 'PASSED'},
                    {time: {start: 5, duration: 17, stop: 22}, title: 'test2', uid: 'uid2', status: 'BROKEN'},
                    {time: {start: 15, duration: 11, stop: 26}, title: 'test3', uid: 'uid3', status: 'PASSED'}
                ],
                onItemClick: jasmine.createSpy('onItemClick')
            }
        );
        $timeout.flush();
    }

    beforeEach(module('allure.charts.timeline', function($provide, $filterProvider) {
        $filterProvider.register('time', function() {
            return angular.identity;
        });
    }));
    jasmine.qatools.mockD3Tooltip();

    it('should create and update timeline', function() {
        createTimeline();
        expect(elem.find('.bar').length).toBe(3);
        scope.times.push({time: {start: 23, duration: 7, stop: 30}, title: 'test4', uid: 'uid4', status: 'FAILED'});
        scope.$apply();
        $timeout.flush();
        expect(elem.find('.bar').length).toBe(4);
    });

    it('should filter tests with zero duration', function() {
        createTimeline();
        scope.times.push({time: {start: 33, duration: 0, stop: 33}, title: 'test4', uid: 'uid4', status: 'FAILED'});
        scope.$apply();
        $timeout.flush();
        expect(elem.find('.bar').length).toBe(3);
    });

    it('should clear timeline when there is no tests', function() {
        createTimeline();
        scope.times = [];
        scope.$apply();
        $timeout.flush();
        expect(elem.find('.bar').length).toBe(0);
    });

    it('should group simultaneous timestamps', function() {
        createTimeline();
        expect(elem.find('.chart-group>g').length).toBe(2);
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
