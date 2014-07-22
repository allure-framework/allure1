/*globals angular*/
angular.module('allure.xUnit.controllers', [])
    .controller('HomeCtrl', function ($scope, $state, testsuites) {
        'use strict';
        function setTestsuite(testsuiteUid) {
            var testsuite = $scope.testsuites.filter(function(testsuite) {
                return testsuite.uid === testsuiteUid;
            })[0];
            if($scope.testsuite !== testsuite) {
                $scope.testsuite = testsuite;
            }
        }

        $scope.setTestsuite = function(testsuiteUid) {
            $state.go('home.testsuite', {testsuiteUid: testsuiteUid});
        };
        $scope.isState = function(statename) {
            return $state.is(statename);
        };
        $scope.testsuites = testsuites.testSuites;
        $scope.time = testsuites.time;
        $scope.statistic = $scope.testsuites.reduce(function(statistic, testsuite) {
            ['passed', 'pending', 'canceled', 'broken', 'failed', 'total'].forEach(function(status) {
                statistic[status] += testsuite.statistic[status];
            });
            return statistic;
        }, {
            passed: 0, pending: 0, canceled: 0, failed: 0, broken: 0, total: 0
        });
        $scope.testcase = {};
        $scope.$watch('testcase.uid', function(testcaseUid, oldUid) {
            if(testcaseUid && testcaseUid !== oldUid) {
                $state.go('home.testsuite.testcase', {testcaseUid: testcaseUid});
            }
        });
        $scope.$on('$stateChangeSuccess', function(event, state, params) {
            delete $scope.testsuite;
            delete $scope.testcase.uid;
            if(params.testsuiteUid) {
                setTestsuite(params.testsuiteUid);
            }
            if(params.testcaseUid) {
                $scope.testcase.uid = params.testcaseUid;
            }
        });
    })

    .controller('TestSuitesCtrl', function($scope, WatchingStore, Collection) {
        'use strict';
        var store = new WatchingStore('testSuitesSettings');
        $scope.statusFilter = function(testsuite) {
            var visible = false;
            angular.forEach(testsuite.statistic, function(value, status) {
                visible = visible || ($scope.showStatuses[status.toUpperCase()] && value > 0);
            });
            return visible;
        };
        $scope.select = function(direction) {
            var index = $scope.list.indexOf($scope.testsuite),
                testsuite = direction < 0 ? $scope.list.getPrevious(index) : $scope.list.getNext(index);
            $scope.setTestsuite(testsuite.uid);
        };
        $scope.showStatuses = {};
        $scope.sorting = {
            predicate: store.bindProperty($scope, 'sorting.predicate', 'statistic.failed'),
            reverse: store.bindProperty($scope, 'sorting.reverse', false)
        };
        $scope.$watch('testsuites', function(testsuites) {
            $scope.list = new Collection(testsuites);
        });
        $scope.$watch('showStatuses', function() {
            $scope.list.filter($scope.statusFilter);
        }, true);
        $scope.$watch('sorting', function() {
            $scope.list.sort($scope.sorting);
        }, true);
    });
