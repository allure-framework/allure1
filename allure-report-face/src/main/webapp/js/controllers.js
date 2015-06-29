/*global angular:true */
angular.module('allure.core.controllers', [])
    .controller('OverviewCtrl', function($scope, $storage, widgets) {
        "use strict";

        var store = $storage('allure-widgets-' + widgets.hash),
            storedWidgets = store.getItem('widgets') || widgets.data.reduce(function(all, widget, index) {
            all[index % 2].push(widget.name);
            return all;
        }, [[], []]);

        $scope.widgets = storedWidgets.map(function(col) {
            return col.map(function(widgetName) {
                return widgets.data.filter(function(widget) {
                    return widget.name === widgetName;
                })[0];
            })
        });

        $scope.onSort = function() {
            store.setItem('widgets', $scope.widgets.map(function(col) {
                return col.map(function(widget) {
                    return widget.name;
                });
            }));
        };

        $scope.sortableConfig = {
            group: 'widgets',
            handle: ".widget_handle",
            ghostClass: "widget-dragged",
            onEnd: $scope.onSort
        };
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
            name: "English",
            locale: "en"
        }, {
            name: "Русский",
            locale: "ru"
        },{
           name: "Português",
           locale: "ptbr" 
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
