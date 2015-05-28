/*global angular */
(function() {
    "use strict";
    var module = angular.module('allure.core', [
        //3rd-party
        'pascalprecht.translate',
        'angular-loading-bar',
        'ngAnimate',
        'ng-sortable',
        'ui.bootstrap',
        'localStorageModule',
        'ui.router',

        //allure-core modules
        'allure.core.filters',
        'allure.core.services',
        'allure.core.directives',
        'allure.core.controllers',
        'allure.core.widgets',
        'allure.core.table',
        'allure.core.pane',
        'allure.core.scrollfix',
        'allure.core.charts',
        'allure.core.testcase',

        //plugins api
        'allure.core.pluginApi'
    ]);

    module.config(function($tooltipProvider) {
        $tooltipProvider.options({appendToBody: true});
    });

    module.config(function(cfpLoadingBarProvider) {
        cfpLoadingBarProvider.parentSelector = '.loader';
    });

    module.config(function($httpProvider) {
        $httpProvider.defaults.cache = true;
        $httpProvider.interceptors.push(['$q', function($q) {
            return {
                responseError: function(reason) {
                    if(reason instanceof Error) {
                        reason = {
                            config: {},
                            status: reason.message
                        };
                    }
                    return $q.reject(reason);
                }
            };
        }]);
    });

    module.run(function($rootScope) {
        $rootScope.$on('$stateChangeError', function(event, toState, toParams, fromState, fromParams, rejection) {
            $rootScope.error = rejection;
        });
    });

    module.config(function($stateProvider, $urlRouterProvider) {
        function processResponse(response) {
            return response.data;
        }
        $urlRouterProvider.otherwise("/");
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
                    },
                    widgets: function($http) {
                        return $http.get('data/widgets.json').then(processResponse);
                    }
                }
            });

    });

    module.config(function($translateProvider, $translatePartialLoaderProvider) {
        $translateProvider.addInterpolation('$translateMessageFormatInterpolation');
        $translatePartialLoaderProvider.addPart('translations');
        $translateProvider.useLoader('$translatePartialLoader', {
            urlTemplate: '{part}/{lang}.json'
        });
        $translateProvider.preferredLanguage('en');
        $translateProvider.fallbackLanguage('en');
    });
    angular.module('d3', []).constant('d3', window.d3);
})();
