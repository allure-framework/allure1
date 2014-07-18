/*global describe:true, it:true, beforeEach:true, afterEach:true, expect:true, spyOn:true, module:true, inject:true, angular:true, jasmine:true */
describe('Testcases status switcher', function () {
    'use strict';
    var store;

    beforeEach(module('allure.testcase.statusSwitcher'));
    beforeEach(module('allure.services'));
    beforeEach(module(function($provide) {
        store = {};
        $provide.value('$storage', function() {
            return {
                getItem: function (key) {
                    return angular.isDefined(store[key]) ? store[key] : null;
                },
                setItem: function (key, value) {
                    store[key] = value;
                },
                removeItem: function(key) {
                    delete store[key];
                }
            };
        });
    }));
    //FIXME: duplicate of template
    beforeEach(inject(function($templateCache) {
        $templateCache.put('templates/testcase/status-switcher.html', '<div class="btn-group status_switcher">'+
            '<span class="btn btn-default" ng-repeat="status in sortedStatuses" '+
            'ng-click="toggle(status)" ng-class="getClassName(status)">{{status | lowercase}} {{getStatistic(status)}}</span>'+
    '</div>')
    }));

    function createElement(scopeVal) {
        var scope, elem;
        inject(function ($compile, $rootScope) {
            scope = $rootScope.$new();
            angular.extend(scope, scopeVal);
            elem = angular.element('<div status-switcher statuses="status" statistic="statistic"></div>');
            $compile(elem)(scope);
            scope.$digest();
        });
        //noinspection JSUnusedAssignment
        return {
            scope: scope,
            el: elem
        }
    }

    function assertCheckedStatuses(elem, statuses) {
        statuses.forEach(function(status) {
            expect(elem.find('.btn.btn-status-'+status)).toHaveClass('active');
        });
        expect(elem.find('.btn.active').length).toBe(statuses.length);

    }

    it('should create switcher and set default values', function() {
        var switcher = createElement({});
        assertCheckedStatuses(switcher.el, ['canceled', 'broken', 'failed'])
    });

    it('should expose statuses into parent scope', function() {
        var switcher = createElement({});
        expect(switcher.scope.status).toEqual({PASSED: false, CANCELED: true, FAILED: true, BROKEN: true, PENDING: false})
    });

    it('should create switcher load values from store', function() {
        store = {PASSED: true, FAILED: false};
        var switcher = createElement({});
        assertCheckedStatuses(switcher.el, ['canceled', 'broken', 'passed'])
    });

    it('should toggle status and save it into store', function() {
        store = {PASSED: true, FAILED: false};
        var switcher = createElement({});
        switcher.el.find('.btn.btn-status-passed').click();
        assertCheckedStatuses(switcher.el, ['canceled', 'broken']);
        expect(store.PASSED).toBe(false);
    });

    it('should share store beetween two switchers', function() {
        var switcher = createElement({}),
            switcher2 = createElement({});
        switcher.el.find('.btn.btn-status-failed').click();
        assertCheckedStatuses(switcher.el, ['canceled', 'broken']);
        assertCheckedStatuses(switcher2.el, ['canceled', 'broken']);
    });

    it('should create buttons in correct order', function() {
        var switcher = createElement({});
        expect(switcher.el.find('.btn').map(function(index, btn) {
            return angular.element(btn).text().trim();
        }).toArray()).toEqual(['failed', 'broken', 'canceled', 'pending', 'passed'])
    });

    describe('statistics', function() {
        it('should not show when statistics is not defined', function() {
            var switcher = createElement({});
            expect(switcher.el.find('.btn.btn-status-failed').text().trim()).toBe('failed');
        });

        it('should show value when it is defined', function() {
            var switcher = createElement({statistic: {failed: 2}});
            expect(switcher.el.find('.btn.btn-status-failed').text().trim()).toBe('failed (2)');
        });

        it('should show zero values when it is defined', function() {
            var switcher = createElement({statistic: {failed: 0}});
            expect(switcher.el.find('.btn.btn-status-failed').text().trim()).toBe('failed (0)');
        });
    });
});
