/*global angular*/
(function() {
    "use strict";
    var module = angular.module('allure.environment', []);

    module.config(['$stateProvider', 'allureTabsProvider', function($stateProvider, allureTabsProvider) {
        allureTabsProvider.addTab('environment', {title: 'environment.TITLE', icon: 'fa fa-cogs'});
        allureTabsProvider.addTranslation('environment');
    }]);
    module.controller('EnvironmentCtrl', ['$scope', '$state', 'data', function($scope, $state, data) {
        "use strict";

        $scope.environment = data;
    }]);
})();
