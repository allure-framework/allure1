/*global angular*/
(function() {
    "use strict";
    var module = angular.module('allure.graph', []);
    module.config(function(allureTabsProvider) {
        allureTabsProvider.addTab('graph', {title: 'graph.TITLE', icon: 'fa fa-bar-chart'});
        allureTabsProvider.addTranslation('graph');
    });
    module.controller('GraphCtrl', function($scope, data, status) {
        $scope.testcases = data.testCases;
        $scope.statistic = {
            passed: 0, canceled: 0, failed: 0, broken: 0, pending: 0,
            total: $scope.testcases.length
        };
        $scope.testcases.forEach(function(testcase) {
            $scope.statistic[testcase.status.toLowerCase()]++;
        });
        $scope.testsPassed = ($scope.statistic.failed + $scope.statistic.broken) === 0;
        $scope.chartData = status.all.map(function(statusName) {
            var value = $scope.statistic[statusName.toLowerCase()];
            return {
                name: statusName.toLowerCase(),
                value: value,
                part: value/$scope.statistic.total
            };
        }, this);
    });
})();
