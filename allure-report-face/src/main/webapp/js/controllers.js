/*global angular:true */
angular.module('allure.controllers', [])
    .controller('GraphCtrl', function($scope, testcases, status) {
        "use strict";
        $scope.testcases = testcases.testCases;
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
    })

    .controller('TimelineCtrl', function($scope, testcases) {
        "use strict";
        $scope.testcases = testcases.testCases;
        $scope.startTime = $scope.testcases.reduce(function(min, testcase) {
            return Math.min(min, testcase.time.start);

        }, Number.POSITIVE_INFINITY);
    })

    .controller('NavbarCtrl', function($scope, $http) {
        'use strict';
        return $http.get('data/report.json').then(function(response) {
            $scope.report = response.data;
        });
    })

    .controller('TabsController', function($scope, $state) {
        'use strict';
        $scope.isCurrent = function(state) {
            return $state.includes(state);
        };
    });