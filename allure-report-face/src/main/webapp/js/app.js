/*global angular */
angular.module('allure', ['ngAnimate', 'ui.bootstrap', 'localStorageModule', 'ui.router',
        'allure.filters', 'allure.services', 'allure.directives', 'allure.controllers', 'allure.table', 'allure.pane',
        'allure.scrollfix', 'allure.charts', 'allure.testcase', 'allure.xUnit.controllers', 'allure.features',
        'allure.defects'])
    .config(function($tooltipProvider) {
        "use strict";
        $tooltipProvider.options({appendToBody:true});
    })
    .config(function($httpProvider) {
        "use strict";
        $httpProvider.defaults.cache = true;
    })
    .run(function($rootScope) {
        "use strict";
        $rootScope.$on('$stateChangeError', function(event, toState, toParams, fromState, fromParams, rejection) {
            $rootScope.error = rejection;
        });
    })
    .config(function ($stateProvider, $urlRouterProvider, testcaseProvider) {
        'use strict';
        function processResponse(response) {
            return response.data;
        }
        function testcasesResolve($http) {
            return $http.get('data/graph.json').then(processResponse);
        }
        $urlRouterProvider.otherwise("/home");
        $stateProvider
            .state('defects', {
                url: '/defects',
                templateUrl: "templates/defects.html",
                controller: 'DefectsCtrl',
                resolve: {
                    defects: function($http) {
                        return $http.get('data/defects.json').then(processResponse);
                    }
                }
            })
            .state('defects.defect', {
                url: "/:defectUid"
            })
            .state('defects.defect.expanded', {
                url: '/expanded'
            })
            .state('home', {
                url: "/home",
                templateUrl: "templates/home.html",
                controller: 'HomeCtrl',
                resolve: {
                    testsuites: function($http) {
                        return $http.get('data/xunit.json').then(processResponse);
                    }
                }
            })
            .state('home.testsuite', {
                url: "/:testsuiteUid"
            })
            .state('home.testsuite.expanded', {
                url: '/expanded'
            })
            .state('graph', {
                url: '/graph',
                templateUrl: "templates/graph.html",
                controller: 'GraphCtrl',
                resolve: {
                    testcases: testcasesResolve
                }
            })
            .state('timeline', {
                url: '/timeline',
                templateUrl: "templates/timeline.html",
                controller: 'TimelineCtrl',
                resolve: {
                    testcases: testcasesResolve
                }
            })
            .state('features', {
                url: '/features',
                templateUrl: "templates/features.html",
                controller: 'FeaturesCtrl',
                resolve: {
                    features: function($http) {
                        return $http.get('data/behavior.json').then(processResponse);
                    }
                }
            })
            .state('features.story', {
                url: '/:storyUid'
            })
            .state('features.story.expanded', {
                url: '/expanded'
            });
        testcaseProvider.attachStates('defects.defect');
        testcaseProvider.attachStates('features.story');
        testcaseProvider.attachStates('home.testsuite');
    });
