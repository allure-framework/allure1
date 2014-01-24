/*global describe:true, it:true, beforeEach:true, afterEach:true, expect:true, spyOn:true, module:true, inject:true, angular:true, jasmine:true */
describe('xUnit controllers', function () {
    'use strict';
    var $controller, $rootScope;

    beforeEach(module('allure.xUnit.controllers'));
    beforeEach(inject(function (_$controller_, _$rootScope_) {
        $controller = _$controller_;
        $rootScope = _$rootScope_;
    }));

    describe('TestSuitesCtrl', function() {
        var watchingStoreSpy;
        function createController() {
            var scope = $rootScope.$new();
            scope.testsuites = [
                {uid: 0, statistic: {passed: 0, failed: 1, broken: 0, skipped: 0}},
                {uid: 1, statistic: {passed: 3, failed: 0, broken: 0, skipped: 0}},
                {uid: 2, statistic: {passed: 2, failed: 0, broken: 2, skipped: 1}}
            ];
            $controller('TestSuitesCtrl', {
                $scope: scope,
                WatchingStore: function() {
                    return watchingStoreSpy = jasmine.createSpyObj('WatchingStore', ['bindProperty'])
                }
            });
            scope.showStatuses = {PASSED: false, BROKEN: true, FAILED: true, SKIPPED: true};
            $rootScope.$apply();
            return scope;
        }

        it('should bind sort settings to storage', function () {
            createController();
            expect(watchingStoreSpy.bindProperty.calls.length).toBe(2);
        });

        it('should filter testsuites by status', function() {
            var scope = createController();
            expect(scope.testsuites.filter(scope.statusFilter).length).toBe(2);
            scope.showStatuses.PASSED = true;
            expect(scope.testsuites.filter(scope.statusFilter).length).toBe(3);
        });
    });

    describe('HomeCtrl', function() {
        var state,
            scope;

        function createController(testsuites) {
            var scope = $rootScope.$new();
            $controller('HomeCtrl', {
                $scope: scope,
                $state: state = {
                    current: {data: {baseState: 'base'}},
                    go: jasmine.createSpy('gotoStateSpy'),
                    is: jasmine.createSpy('isStateSpy')
                },
                testsuites: {testSuites: testsuites}
            });
            return scope;
        }

        function TestSuite(uid, start, statistic) {
            this.uid = uid;
            this.time = {start: start};
            this.statistic = statistic;
            this.testCases = Array.apply(null, new Array(statistic.total)).map(function (_, i) {
                return {uid: 'case'+i};
            });
        }

        beforeEach(function() {
            scope = createController([
                new TestSuite('suite1', 16545, {passed: 2, skipped: 0, broken: 0, failed: 1, total: 3}),
                new TestSuite('suite2', 35335, {passed: 2, skipped: 0, broken: 1, failed: 1, total: 4}),
                new TestSuite('suite3', 42566, {passed: 1, skipped: 0, broken: 0, failed: 0, total: 1})
            ]);
            scope.$apply();
        });

        it('should change state when select is called', function() {
            expect(state.go).not.toHaveBeenCalled();
            scope.setTestsuite('suite3');
            expect(state.go).toHaveBeenCalledWith('home.testsuite', {testsuiteUid: 'suite3'});
            scope.$apply("testcase.uid = 'case1'");
            expect(state.go).toHaveBeenCalledWith('home.testsuite.testcase', {testcaseUid: 'case1'});
        });

        it('should add up overall testsuites statistics', function() {
            expect(scope.statistic).toEqual({
                passed: 5, skipped: 0, broken: 1, failed: 2, total: 8
            })
        });

        describe('transitions', function() {
            function getObjectValues(object) {
                return Object.keys(object).map(function(key) {
                    return object[key];
                });
            }

            function copyFields(object, fields) {
                return fields.reduce(function(result, field) {
                    result[field+'Uid'] = object[field+'Uid'];
                    return result;
                }, {})
            }

            function assertState(testsuite, testcase) {
                expect(scope.testsuite)[testsuite ? 'toBeDefined' : 'toBeUndefined']();
                expect(scope.testcase.uid)[testcase ? 'toBeDefined' : 'toBeUndefined']();
            }

            var levels = ['testrun', 'testsuite', 'testcase', 'attachment'],
                testValues = {testsuiteUid: 'suite2', testcaseUid:'case7'},
                switchValues = {testsuiteUid: 'suite3', testcaseUid:'case5'};

            function makeReturnTest(initialLevel, targetLevel) {
                var source = copyFields(testValues, levels.slice(1, levels.indexOf(initialLevel))),
                    dest = copyFields(testValues, levels.slice(1, levels.indexOf(targetLevel)));
                makeTransitionTest(source, dest, 'should return to '+targetLevel+' from '+initialLevel)
            }
            function makeSwitchTest(level) {
                var index = levels.indexOf(level),
                    source = copyFields(testValues, levels.slice(1, index)),
                    dest = copyFields(switchValues, levels.slice(1, index));
                makeTransitionTest(source, dest, 'should switch between '+level+'s');
            }
            function makeSelectTest(level) {
                var index = levels.indexOf(level),
                    source = copyFields(testValues, levels.slice(1, index-1)),
                    dest = copyFields(testValues, levels.slice(1, index));
                makeTransitionTest(source, dest, 'should select '+level+' from '+levels[index-1]);
            }
            function makeTransitionTest(source, dest, description) {
                it(description, function() {
                    scope.$broadcast('$stateChangeSuccess',  null, source);
                    assertState.apply(null, getObjectValues(source));
                    scope.$broadcast('$stateChangeSuccess',  null, dest);
                    assertState.apply(null, getObjectValues(dest));
                });
            }

            levels.forEach(function(level, index) {
                if(index > 0) {
                    makeSwitchTest(level);
                    makeSelectTest(level);
                    for(var i = index;i>0;i--) {
                        makeReturnTest(level, levels[index-i]);
                    }
                }
            });
        });
    });
});
