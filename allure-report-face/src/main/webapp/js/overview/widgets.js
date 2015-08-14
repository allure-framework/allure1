(function() {
    var module = angular.module('allure.core.widgets', []);
    module.directive('widgetStatistics', ['percents', function(percents) {
        return {
            restrict: 'E',
            templateUrl: 'templates/overview/statistics.html',
            scope: {
                data: '='
            },
            link: function($scope) {
                $scope.data.forEach(function(data) {
                    data.percents = percents(data.statistic);
                });
            }
        };
    }]);
    module.directive('widgetMessages', [function() {
        return {
            restrict: 'E',
            templateUrl: 'templates/overview/defects.html',
            scope: {
                data: '='
            },
            link: function($scope) {
            }
        };
    }]);
    module.directive('widgetTotal', ['percents', function(percents) {
        return {
            restrict: 'E',
            templateUrl: 'templates/overview/total.html',
            scope: {
                data: '='
            },
            link: function($scope) {
                $scope.percents = percents($scope.data.statistic);
            }
        };
    }]);
    module.directive('widgetKeyValue', [function() {
        return {
            restrict: 'E',
            templateUrl: 'templates/overview/keyValue.html',
            scope: {
                data: '='
            },
            controllerAs: 'ctrl',
            controller: ['$scope', function($scope) {
                this.dataLimit = 10;
                this.showAll = function() {
                    this.dataLimit = $scope.data.length;
                }
            }]
        };
    }]);
})();