/*global angular*/
angular.module('allure.core.testcase.testcasesList', []).directive('testcasesList', function() {
    'use strict';
    return {
        templateUrl: 'templates/testcase/list.html',
        controller: 'TestCasesCtrl',
        scope: {
            testcases: '=testcasesList',
            showStatuses: '=statuses',
            selectedUid: '='
        }
    };
}).controller('TestCasesCtrl', ['$scope', 'WatchingStore', 'status', 'severity', 'Collection', function ($scope, WatchingStore, status, severity, Collection) {
    'use strict';
    var store = new WatchingStore('testCasesSettings');
    $scope.bySeverity = function(testcase) {
        return severity.getSortOrder(testcase.severity);
    };
    $scope.byStatus = function (testcase) {
        return status.getSortOrder(testcase.status);
    };
    $scope.byStatusAndSeverity = [$scope.byStatus, $scope.bySeverity];

    $scope.statusFilter = function (testcase) {
        return $scope.showStatuses && $scope.showStatuses[testcase.status];
    };
    $scope.getVisibleCount = function() {
        var count = 0;
        angular.forEach($scope.statistic, function(value, status) {
            if($scope.showStatuses && $scope.showStatuses[status.toUpperCase()]) {
                count += value;
            }
        });
        return count;
    };
    $scope.selectTestcase = function(testcase) {
        $scope.selectedUid = testcase.uid;
    };
    $scope.select = function(direction) {
        var currentIndex = $scope.list.getIndexBy('uid', $scope.selectedUid),
            testcase;
        if(direction < 0) {
            testcase = $scope.list.getPrevious(currentIndex);
        }
        else {
            testcase = $scope.list.getNext(currentIndex);
        }
        $scope.selectTestcase(testcase);
    };
    $scope.testcasesLimit = 100;
    $scope.sorting = {
        predicate: store.bindProperty($scope, 'sorting.predicate', $scope.byStatusAndSeverity),
        reverse: store.bindProperty($scope, 'sorting.reverse', false)
    };
    $scope.$watch('testcases', function(testcases) {
        testcases.sort(function (caseA, caseB) {
            return caseA.time.start - caseB.time.start;
        }).forEach(function (testcase, index) {
            testcase.order = index + 1;
        });
        $scope.statistic = testcases.reduce(function(statistic, testcase) {
            statistic[testcase.status.toLowerCase()]++;
            return statistic;
        }, {
            passed: 0, canceled: 0, failed: 0, broken: 0, pending: 0, total: testcases.length
        });
        $scope.list = new Collection(testcases);
        $scope.list.sort($scope.sorting);
        $scope.list.limitTo($scope.testcasesLimit);
        $scope.list.filter($scope.statusFilter);
    });
    $scope.$watch('sorting', function(sorting) {
        $scope.list.sort(sorting);
    }, true);
    $scope.$watch('testcasesLimit', function(limit) {
        $scope.list.limitTo(limit);
    });
    $scope.$watch('showStatuses', function() {
        $scope.list.filter($scope.statusFilter);
    }, true);
}]);
