/*global describe:true, it:true, beforeEach:true, afterEach:true, expect:true, spyOn:true, module:true, inject:true, angular:true, jasmine:true */
describe('xUnit controllers', function () {
    'use strict';
    var $controller, $rootScope;

    beforeEach(module('allure.xUnit.controllers'));
    beforeEach(inject(function (_$controller_, _$rootScope_) {
        $controller = _$controller_;
        $rootScope = _$rootScope_;
    }));

    describe('TestCasesCtrl', function () {
        var watchingStoreSpy;
        function createController() {
            var scope = $rootScope.$new();
            scope.testcases = [
                {uid: 0, time:{start: 0}, status: 'PASSED'},
                {uid: 1, time:{start: 42}, status: 'PASSED'},
                {uid: 2, time:{start: 44}, status: 'BROKEN'},
                {uid: 3, time:{start: 33}, status: 'FAILED'}
            ];
            scope.testsuite = {
                statistic: { passed: 2, broken: 1, failed: 1, skipped: 0 }
            };
            $controller('TestCasesCtrl', {
                $scope: scope,
                WatchingStore: function() {
                    return watchingStoreSpy = jasmine.createSpyObj('WatchingStore', ['bindProperty'])
                },
                status: {},
                severity: {}
            });
            scope.showStatuses = {PASSED: false, BROKEN: true, FAILED: true, SKIPPED: true};
            $rootScope.$apply();
            return scope;
        }

        it('should bind sort settings to storage', function () {
            createController();
            expect(watchingStoreSpy.bindProperty.calls.length).toBe(2);
        });

        it('should filter testcases by status', function() {
            var scope = createController();


            expect(scope.statusFilter({status: 'PASSED'})).toBe(false);
            expect(scope.statusFilter({status: 'FAILED'})).toBe(true);
            scope.showStatuses = {PASSED: true, BROKEN: true, FAILED: true, SKIPPED: false};

            expect(scope.statusFilter({status: 'PASSED'})).toBe(true);
        });

        it('should set order based on start time', function() {
            var scope = createController();
            expect(scope.testcases[0].order).toEqual(1);
            expect(scope.testcases[0].uid).toEqual(0);
            expect(scope.testcases[3].uid).toEqual(2);
            expect(scope.testcases[3].order).toEqual(4);
        });

        it('should find visible testcases count', function() {
            var scope = createController();
            expect(scope.getVisibleCount()).toBe(2);
        });
    });

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

    xdescribe('HomeCtrl', function() {
        var treeUtilsSpy,
            stateMock,
            scope;
        function StateMock() {
            this.go = jasmine.createSpy('goSpy');
            this.state = false;
            this.is = function(name) {
                return this.state === name || false;
            };
            this.includes = function(name) {
                return this.state.substring(0, name.length) === name;
            }
        }

        function createController(testsuites, testcases) {
            var scope = $rootScope.$new();
            stateMock = new StateMock();
            $controller('HomeCtrl', {
                $scope: scope,
                $state: stateMock,
                treeUtils: treeUtilsSpy = {walkAround: jasmine.createSpy('treeWalkSpy').andCallFake(function(testcase, prop, callback) {
                    callback(testcase);
                })},
                testsuites: {testSuites: testsuites},
                testcases: {testCases: testcases}
            });
            return scope;
        }

        beforeEach(function() {
            scope = createController([
                {time: {start:16545}, uid: 'suite1', statistic: {passed: 2, skipped: 0, broken: 0, failed: 1, total: 3}},
                {time: {start:35335}, uid: 'suite2', statistic: {passed: 2, skipped: 0, broken: 1, failed: 1, total: 4}},
                {time: {start:42566}, uid: 'suite3', statistic: {passed: 1, skipped: 0, broken: 0, failed: 0, total: 1}}
            ], [
                {uid: 'case1', suiteUid: 'suite1', title: 'Original testcase'},
                {uid: 'case2', suiteUid: 'suite1'},
                {uid: 'case3', suiteUid: 'suite1'},
                {uid: 'case4', suiteUid: 'suite2'},
                {uid: 'case5', suiteUid: 'suite2'},
                {uid: 'case6', suiteUid: 'suite2'},
                {uid: 'case7', suiteUid: 'suite2', attachments: [{source: 'file.txt'}, {source: 'image.jpg'}]},
                {uid: 'case8', suiteUid: 'suite3'},
                {uid: 'case1', suiteUid: 'suite3', title: 'Yet another testcase'}
            ])
        });

        it('should change state when select is called', function() {
            expect(stateMock.go).not.toHaveBeenCalled();
            scope.setTestsuite('suite3');
            expect(stateMock.go).toHaveBeenCalledWith('home.testsuite', {testsuiteUid: 'suite3'});
            scope.setTestcase('case1');
            expect(stateMock.go).toHaveBeenCalledWith('home.testsuite.testcase', {testcaseUid: 'case1'});
            scope.setAttachment('file.txt');
            expect(stateMock.go).toHaveBeenCalledWith('home.testsuite.testcase.attachment', {attachmentUid: 'file.txt'});
        });

        it('should add up overall testsuites statistics', function() {
            expect(scope.statistic).toEqual({
                passed: 5, skipped: 0, broken: 1, failed: 2, total: 8
            })
        });

        it('should select correct testcase when testcase uid is not unique', function() {
            scope.$broadcast('$stateChangeSuccess',  null, {testsuiteUid:'suite3', testcaseUid:'case1'});
            expect(scope.testcase.title).toBe('Yet another testcase');
        });

        describe('transitions', function() {
            function getObjectValues(object) {
                return Object.keys(object).map(function(key) {
                    return object[key];
                });
            }

            function assertState(testsuite, testcase, attachment) {
                if(testsuite) {
                    expect(scope.testsuite.uid).toBe(testsuite);
                }
                else {
                    expect(scope.testsuite).toBeUndefined();
                }
                if(testcase) {
                    expect(scope.testcase.uid).toBe(testcase);
                }
                else {
                    expect(scope.testcase).toBeUndefined();
                }
                if(attachment) {
                    expect(scope.attachment.source).toBe(attachment);
                }
                else {
                    expect(scope.attachment).toBeUndefined();
                }
            }

            var levels = ['testrun', 'testsuite', 'testcase', 'attachment'],
                testValues = {testsuiteUid: 'suite2', testcaseUid:'case7', attachmentUid:'file.txt'},
                switchValues = {testsuiteUid: 'suite3', testcaseUid:'case5', attachmentUid:'image.jpg'};

            function makeReturnTest(initialLevel, targetLevel) {
                var initialLevelIndex = levels.indexOf(initialLevel),
                    targetLevelIndex = levels.indexOf(targetLevel),
                    source = {},
                    dest = {};
                for(var i=1;i<=initialLevelIndex;i++) {
                    var key = levels[i]+'Uid';
                    if(i <= targetLevelIndex) {
                        dest[key] = testValues[key];
                    }
                    source[key] = testValues[key];
                }
                makeTransitionTest(source, dest, 'should return to '+targetLevel+' from '+initialLevel)
            }
            function makeSwitchTest(level) {
                var levelIndex = levels.indexOf(level),
                    source = {},
                    dest = {};
                for(var i=1;i<=levelIndex;i++) {
                    var key = levels[i]+'Uid';
                    dest[key] = i < levelIndex ? testValues[key] : switchValues[key];
                    source[key] = testValues[key];
                }
                makeTransitionTest(source, dest, 'should switch between '+level+'s');
            }
            function makeSelectTest(level) {
                var levelIndex = levels.indexOf(level),
                    source = {},
                    dest = {};
                for(var i=1;i<=levelIndex;i++) {
                    var key = levels[i]+'Uid';
                    if(i < levelIndex) {
                        source[key] = testValues[key];
                    }
                    dest[key] = testValues[key];
                }
                makeTransitionTest(source, dest, 'should select '+level+' from '+levels[levelIndex-1]);
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

            it('should filter testcases when switching to testsuite', function() {
                scope.$broadcast('$stateChangeSuccess',  null, {testsuiteUid:'suite2'});
                expect(scope.testcases.length).toBe(4);
            });
        });

        describe('state checker', function() {
            function testStateCheck(state, sample, result) {
                stateMock.state = sample;
                expect(scope.isState(state)).toBe(result);
            }
            it('should check current state', function() {
                testStateCheck('home', 'home', true);
                testStateCheck('home.testsuite', 'home.testsuite', true);
                testStateCheck('home', 'home.testsuite', false);
            });
            it('should support wildcards in state check', function() {
                testStateCheck('home.*', 'home.testsuite.expanded', true);
                testStateCheck('home.testsuite.*', 'home.testsuite', false);
            });
        });
    });
});
