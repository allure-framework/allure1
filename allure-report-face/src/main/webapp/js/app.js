/*global angular */
angular.module('allure', ['pascalprecht.translate', 'angular-loading-bar', 'ngAnimate', 'ui.bootstrap', 'localStorageModule', 'ui.router',
        'allure.filters', 'allure.services', 'allure.directives', 'allure.controllers', 'allure.table', 'allure.pane',
        'allure.scrollfix', 'allure.charts', 'allure.testcase', 'allure.xUnit.controllers', 'allure.features',
        'allure.defects', 'allure.overview'])
    .config(function($tooltipProvider) {
        "use strict";
        $tooltipProvider.options({appendToBody:true});
    })
    .config(function(cfpLoadingBarProvider) {
        "use strict";
        cfpLoadingBarProvider.parentSelector = '.loader';
    })
    .config(function($httpProvider) {
        "use strict";
        $httpProvider.defaults.cache = true;
        $httpProvider.interceptors.push(['$q', function($q) {
            return {
                responseError: function(reason) {
                    if(reason instanceof Error) {
                        reason = {
                            config: {},
                            status: reason.message
                        }
                    };
                    return $q.reject(reason);
                }
            };
        }]);
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
            .state('overview', {
                url: '/',
                templateUrl: "templates/overview.html",
                controller: 'OverviewCtrl',
                resolve: {
                    overview: function($http) {
                        return $http.get('data/environment.json').then(processResponse);
                    },
                    defects: function($http) {
                        return $http.get('data/defects.json').then(processResponse);
                    },
                    testsuites: function($http) {
                        return $http.get('data/xunit.json').then(processResponse);
                    }
                }
            })
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
                    data: function($http) {
                        return $http.get('data/timeline.json').then(processResponse);
                    }
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
        testcaseProvider.attachStates('timeline');
    })
    .config(function($translateProvider) {
        $translateProvider.useStaticFilesLoader({
          prefix: 'translations/',
          suffix: '.json'
        });
        $translateProvider.addInterpolation('$translateMessageFormatInterpolation');
        $translateProvider.preferredLanguage('en');
        $translateProvider.fallbackLanguage('en');
    });
