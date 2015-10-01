/*global angular*/
(function() {
    "use strict";
    angular.module('allure.environment', []).config(function(allurePluginsProvider) {
        allurePluginsProvider.addWidget('environment', {
            title: 'Environment',
            templateUrl: 'templates/overview/keyValue.html',
            controller: 'KeyValueWidgetController'
        });
    });
})();
