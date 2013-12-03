/*global angular */
angular.module('allure', ['ui.bootstrap', 'localStorageModule', 'ui.router',
        'allure.filters', 'allure.services', 'allure.directives', 'allure.controllers', 'allure.charts',
        'allure.testcase.controllers', 'allure.xUnit.controllers', 'allure.table', 'allure.features'])
    .config(function($tooltipProvider) {
        $tooltipProvider.options({appendToBody:true})
    })
    .config(function ($stateProvider, $urlRouterProvider) {
        'use strict';
        function processResponse(response) {
            return response.data;
        }
        function testcasesResolve($http) {
            return $http.get('data/graph.json').then(processResponse);
        }
        $urlRouterProvider.otherwise("/home");
        $stateProvider
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
            .state('home.testsuite.testcase', {
                url: "/:testcaseUid"
            })
            .state('home.testsuite.testcase.expanded', {
                url: '/expanded'
            })
            .state('home.testsuite.testcase.attachment', {
                url: '/:attachmentUid'
            })
            .state('home.testsuite.testcase.attachment.expanded', {
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
                        return $http.get('data/features.json').then(processResponse);
                    }
                }
            })
            .state('features.story', {
                url: '/:storyUid'
            })
    });