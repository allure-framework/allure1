/*global angular*/
(function() {
    "use strict";
    angular.module('allure.environment', []).config(function(allurePluginsProvider) {
        allurePluginsProvider.addTranslation('environment');
        allurePluginsProvider.addWidget('environment', {
            title: 'environment.TITLE',
            templateUrl: 'templates/overview/keyValue.html',
            controller: 'KeyValueWidgetController'
        });
    });
})();
