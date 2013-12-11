angular.module('allure.testcase.testcasesList', []).directive('testcasesList', function() {
    return {
        templateUrl: 'templates/testcase/list.html',
        controller: 'TestCasesCtrl',
        scope: {
            testcases: '=testcasesList',
            showStatuses: '=statuses',
            selectedUid: '='
        }
    }
}).controller('TestCasesCtrl', ['$scope' , 'WatchingStore', 'status', 'severity', function ($scope, WatchingStore, status, severity) {
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
            passed: 0, skipped: 0, failed: 0, broken: 0, total: testcases.length
        });
    });
}]);