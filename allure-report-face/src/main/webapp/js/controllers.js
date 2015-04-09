/*global angular:true */
angular.module('allure.core.controllers', [])
    .controller('OverviewCtrl', function($scope, orderByFilter, status, percents, overview, defects, testsuites) {
        "use strict";
        $scope.overview = overview;
        $scope.defects = defects.defectsList.filter(function(defect) {
            return defect.defects && defect.defects.length > 0;
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
            name: "ENG",
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

    .controller('TabsController', function($scope, $state, $storage, allureTabs) {
        'use strict';
        var settings = $storage('settings');

        $scope.tabs = allureTabs;
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
