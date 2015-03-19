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

    .controller('TimelineCtrl', function($scope, $state, data) {
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
    })

    .controller('NavbarCtrl', function($scope, $window, $http, $storage, $translate) {
        'use strict';
        function browserLanguage() {
            return $window.navigator.language && $window.navigator.language.split('-').shift();
        }
        var locale = $storage('locale');
        $scope.setLang = function(langKey) {
            $scope.selectedLang = langKey.locale;
            $translate.use($scope.selectedLang);
            locale.setItem('lang', $scope.selectedLang);
        };

        $scope.langs = [{
            name: "ENG" ,
            locale: "en"
        }, {
            name: "РУС",
            locale: "ru"
        }];
        $scope.selectedLang = locale.getItem('lang') || browserLanguage() || 'en';
        $translate.use($scope.selectedLang);

        $http.get('data/report.json').then(function(response) {
            $scope.report = response.data;
        });
    })

    .controller('TabsController', function($scope, $state, $storage) {
        'use strict';
        var settings = $storage('settings');

        $scope.isCollapsed = function() {
            return settings.getItem('collapsed');
        };

        $scope.toggleCollapsed = function() {
            settings.setItem('collapsed', !$scope.isCollapsed());
        };

        $scope.isCurrent = function(state) {
            return $state.includes(state);
        };
    });
