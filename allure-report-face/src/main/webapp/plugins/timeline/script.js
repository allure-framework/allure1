/*global angular*/
(function() {
    "use strict";
    var module = angular.module('allure.timeline', []);
    module.config(function($stateProvider, allureTabsProvider, testcaseProvider) {
        allureTabsProvider.addTab('timeline', {title: 'timeline.TITLE', icon: 'fa fa-clock-o'});
        testcaseProvider.attachStates('timeline');
        allureTabsProvider.addTranslation('timeline');
    });
    module.controller('TimelineCtrl', function($scope, $state, data) {
        "use strict";
        $scope.isState = function(statename) {
            return $state.is(statename);
        };
        $scope.openTestcase = function(testcase) {
            $state.go('timeline.testcase', {testcaseUid: testcase.uid});
        };
        $scope.hosts = data.hosts;
        $scope.allCases = $scope.hosts.reduce(function(result, host) {
            return host.threads.reduce(function(result, thread) {
                return result.concat(thread.testCases);
            }, result);
        }, []);
        $scope.startTime = $scope.allCases.reduce(function(min, testcase) {
            return Math.min(min, testcase.time.start);
        }, Number.POSITIVE_INFINITY);
        $scope.allCases.forEach(function(testcase) {
            testcase.time.start -= $scope.startTime;
            testcase.time.stop -= $scope.startTime;
        });
        $scope.timeRange = [0, $scope.allCases.reduce(function(max, testcase) {
            return Math.max(max, testcase.time.stop);
        }, Number.NEGATIVE_INFINITY)];
    });
})();
