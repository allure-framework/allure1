/*global angular*/
angular.module('allure.core.table', [])
    .directive('allureTable', function () {
        "use strict";
        return {
            replace: true,
            transclude: true,
            templateUrl: 'templates/table/table.html',
            scope: {
                sorting: '=?'
            },
            controller: 'AllureTableController',
            link: function (scope, elem, attrs, ctrl) {
                ctrl.transclude(function (clone) {
                    elem.append(clone);
                });
            }
        };
    })
    .directive('allureTableCol', function () {
        "use strict";
        return {
            require: '^allureTable',
            link: function (scope, elem, attrs, tableCtrl) {
                tableCtrl.addColumn(scope.$eval(attrs.allureTableCol));
            }
        };
    })
    .controller('AllureTableController', function ($scope, $transclude) {
        "use strict";
        var defaultConfig = {reverse: false, flex: 1};
        this.addColumn = function (column) {
            if (typeof column === 'string') {
                column = {
                    heading: column,
                    predicate: column.toLowerCase()
                };
            }
            column = angular.extend({}, defaultConfig, column);
            if (!this.findColumn(column.heading)) {
                $scope.columns.push(column);
            }
        };
        this.findColumn = function (heading) {
            return $scope.columns.filter(function (col) {
                return col.heading === heading;
            })[0];
        };
        this.transclude = $transclude;
        $scope.getWidth = function(column) {
            var totalFlex = $scope.columns.reduce(function(total, col) {
                return total + col.flex;
            }, 0);
            return 100/totalFlex*column.flex + '%';
        };
        $scope.columns = [];
        $scope.sorting = $scope.sorting || {};
        $scope.setPredicate = function (column) {
            if (column) {
                var newPredicate = column.predicate;
                if ($scope.sorting.predicate === newPredicate) {
                    $scope.sorting.reverse = !$scope.sorting.reverse;
                }
                else {
                    $scope.sorting.predicate = newPredicate;
                    $scope.sorting.reverse = column.reverse;
                }
            }
            else {
                delete $scope.sorting.predicate;
                delete $scope.sorting.reverse;
            }
        };
    });
