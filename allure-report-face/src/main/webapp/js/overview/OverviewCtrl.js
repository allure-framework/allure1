/*global angular*/
angular.module('allure.overview', []).controller('OverviewCtrl', function($scope, orderByFilter, status, percents, overview, defects, testsuites) {
    "use strict";
    $scope.overview = overview;
    $scope.defects = orderByFilter(defects.defectsList, function(defectType) {
        return status.getSortOrder(defectType.status);
    });
    $scope.defects.forEach(function(defect) {
        defect.defects = orderByFilter(defect.defects, function(defect) {
            return defect.testCases.length;
        }, true);
    });
    $scope.statistic = testsuites.testSuites.reduce(function(statistic, testsuite) {
        ['passed', 'pending', 'canceled', 'broken', 'failed', 'total'].forEach(function(status) {
            statistic[status] += testsuite.statistic[status];
        });
        return statistic;
    }, {
        passed: 0, pending: 0, canceled: 0, failed: 0, broken: 0, total: 0
    });
    $scope.percents = percents($scope.statistic);
});
